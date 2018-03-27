;
;  (C) Copyright 2017, 2018  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns chainring-service.raster-renderer
    "Namespace that contains functions to render drawings into raster images.")


(require '[clojure.string        :as str])

(require '[ring.util.response    :as http-response])
(require '[clojure.tools.logging :as log])
(require '[clojure.data.json     :as json])
(require '[clj-utils.utils       :as utils])

(require '[chainring-service.http-utils   :as http-utils])
(require '[chainring-service.db-interface :as db-interface])


(import java.awt.Color)
(import java.awt.Font)
(import java.awt.BasicStroke)
(import java.awt.RenderingHints)
(import java.awt.Polygon)
(import java.awt.image.BufferedImage)
(import java.io.ByteArrayInputStream)
(import java.io.ByteArrayOutputStream)
(import javax.imageio.ImageIO)


(defn proper-scale?
    "Predicate if the given item contains expected width and height attributes."
    [item width height]
    (and (= (:width item)  width)
         (= (:height item) height)))


(defn get-scale
    "Return scale set up for given width and height values."
    [data width height]
    (let [scales (get data :scales)]
        (first (filter #(proper-scale? % width height) scales))))


(defn transform
    "Transform x or y coordinate using provided scale and offset."
    [coordinate scale offset after-offset]
    (int (+ after-offset (* scale (+ coordinate offset)))))


; TODO: background color to be read from configuration
; TODO: foreground color to be read from configuration
(defn setup-graphics-context
    "Set up graphics context."
    [image gc width height]
    (let [rh (new RenderingHints RenderingHints/KEY_RENDERING RenderingHints/VALUE_RENDER_QUALITY)]
        (.put rh RenderingHints/KEY_TEXT_ANTIALIASING RenderingHints/VALUE_TEXT_ANTIALIAS_ON)
        (.put rh RenderingHints/KEY_ANTIALIASING      RenderingHints/VALUE_ANTIALIAS_ON)
        (.setRenderingHints gc rh))
    (let [font (new Font "Helvetica" Font/PLAIN 8)]
        (.setFont gc font))
    (.setBackground gc (new Color 0.9 0.9 0.8))
    (.clearRect gc 0 0 width height)
    (.setColor gc Color/BLACK)
    (.drawRect gc 0 0 (dec width) (dec height)))


(defn draw-line
    [gc entity scale x-offset y-offset user-x-offset user-y-offset]
    (let [x1 (transform (:x1 entity) scale x-offset user-x-offset)
          y1 (transform (:y1 entity) scale y-offset user-y-offset)
          x2 (transform (:x2 entity) scale x-offset user-x-offset)
          y2 (transform (:y2 entity) scale y-offset user-y-offset)]
          (.drawLine gc x1 y1 x2 y2)))


(defn draw-arc
    [gc entity scale x-offset y-offset user-x-offset user-y-offset]
    (let [x (transform (:x entity) scale x-offset user-x-offset)
          y (transform (:y entity) scale y-offset user-y-offset)
          r (int (* scale (:r entity)))
          a1 (:a1 entity)
          a2 (:a2 entity)
          delta (int (- a2 a1))
          extent (if (neg? delta) (+ delta 360) delta)]
          (.drawArc gc (- x r) (- y r) (* r 2) (*  r 2) a1 extent)))


(defn draw-text
    [gc entity scale x-offset y-offset user-x-offset user-y-offset]
    (let [x (transform (:x entity) scale x-offset user-x-offset)
          y (transform (:y entity) scale y-offset user-y-offset)
          t (:text entity)]
          (.drawString gc t x y)))


(defn draw-entities
    [gc entities scale x-offset y-offset user-x-offset user-y-offset]
    (.setColor gc Color/BLACK)
    (doseq [entity entities]
        (condp = (:T entity) 
            "L" (draw-line gc entity scale x-offset y-offset user-x-offset user-y-offset)
            "A" (draw-arc  gc entity scale x-offset y-offset user-x-offset user-y-offset)
            "T" (draw-text gc entity scale x-offset y-offset user-x-offset user-y-offset)
                nil
        )))


(defn draw-room-background
    [gc xpoints ypoints background-color]
    (.setColor gc background-color)
    (.fillPolygon gc (int-array xpoints)
                     (int-array ypoints)
                     (count xpoints)))


(defn draw-room-contour
    [gc xpoints ypoints foreground-color]
    (.setColor gc foreground-color)
    (.drawPolygon gc (int-array xpoints)
                     (int-array ypoints)
                     (count xpoints)))


(defn draw-selected-room
    [gc xpoints ypoints]
    (draw-room-background gc xpoints ypoints (new Color 1.0 1.0 0.5 0.5))
    (draw-room-contour    gc xpoints ypoints Color/RED))


(defn draw-highlighted-room
    [gc xpoints ypoints aoid room-colors]
    (let [colors (get room-colors aoid)
          foreground-color (:foreground colors)
          background-color (:background colors)]
          (draw-room-background gc xpoints ypoints background-color)
          (draw-room-contour    gc xpoints ypoints foreground-color)))


(defn draw-regular-room
    [gc xpoints ypoints]
    (draw-room-contour gc xpoints ypoints Color/BLUE))


(defn coords-in-polygon
    [xpoints ypoints coordsx coordsy]
    (if (and coordsx coordsy)
        (let [polygon (new Polygon (int-array xpoints) (int-array ypoints) (count xpoints))]
             (.contains polygon (double coordsx) (double coordsy)))))


(defn selected-room?
    [aoid selected transformed-xpoints transformed-ypoints coordsx coordsy user-x-offset user-y-offset]
    (or (= aoid selected) (coords-in-polygon transformed-xpoints transformed-ypoints coordsx coordsy)))


(defn highlighted-room?
    [aoid room-colors]
    (get room-colors aoid nil))


(defn draw-rooms
    [gc rooms scale x-offset y-offset user-x-offset user-y-offset selected room-colors coordsx coordsy]
    (.setStroke gc (new BasicStroke 2))
    (doseq [room rooms]
         (let [polygon (:polygon room)
               aoid    (:room_id room)
               xpoints (map first polygon)
               ypoints (map second polygon)
               transformed-xpoints (map #(transform % scale x-offset user-x-offset) xpoints)
               transformed-ypoints (map #(transform % scale y-offset user-y-offset) ypoints)]
               (if (seq xpoints)
                   (cond
                       (selected-room? aoid selected transformed-xpoints transformed-ypoints coordsx coordsy user-x-offset user-y-offset)
                           (draw-selected-room gc transformed-xpoints transformed-ypoints)
                       (highlighted-room? aoid room-colors)
                           (draw-highlighted-room gc transformed-xpoints transformed-ypoints aoid room-colors)
                       :else (draw-regular-room gc transformed-xpoints transformed-ypoints))))))


(defn draw-selection-point
    [gc x y]
    (when (and x y)
          (.setColor gc (new Color 0.0 0.5 0.0))
          (.drawLine gc (- x 10) y (+ x 10) y)
          (.drawLine gc x (- y 10) x (+ y 10))))


(defn drawing-full-name
    [drawing-id drawing-name]
    (if drawing-id
        (format (str "drawings/%05d.json") (Integer/parseInt drawing-id))
        (if drawing-name
            (str "drawings/" drawing-name))))


(defn drawing-full-name-binary
    [drawing-id drawing-name]
    (if drawing-id
        (format (str "drawings/%05d.bin") (Integer/parseInt drawing-id))
        (if drawing-name
            (str "drawings/" drawing-name))))


(defn read-drawing-from-json
    [filename]
    (let [start-time (System/currentTimeMillis)
          data       (json/read-str (slurp filename) :key-fn clojure.core/keyword)
          end-time   (System/currentTimeMillis)]
          (log/info "JSON loading time (ms):" (- end-time start-time))
          data))


(defn prepare-data-stream
    [filename]
    (let [fis (new java.io.FileInputStream filename)]
        (new java.io.DataInputStream fis)))

(defn draw-into-image-from-binary-data
    [image drawing-id drawing-name width height user-x-offset user-y-offset user-scale
     selected room-colors coordsx coordsy debug]
    (if-let [full-name  (drawing-full-name-binary drawing-id drawing-name)]
        (let [fin            (prepare-data-stream full-name)
              gc             (.createGraphics image)]
            (try
                (let [magic-number   (.readShort fin)
                      file-version   (.readByte fin)
                      data-version   (.readByte fin)
                      created-ms     (.readLong fin)
                      created        (new java.util.Date created-ms)
                      entity-count   (.readInt fin)
                      rooms-count    (.readInt fin)
                      scales-count   (.readInt fin)
                      bounds         {:xmin (.readDouble fin)
                                      :ymin (.readDouble fin)
                                      :xmax (.readDouble fin)
                                      :ymax (.readDouble fin)}
                ]
                    (assert (= magic-number 0x6502))
                    (assert (= file-version 1))
                    (assert (= data-version 1))
                    (log/info "full drawing name" full-name)
                    (log/info "magic number " (Integer/toString magic-number 16))
                    (log/info "file version" file-version)
                    (log/info "data version" data-version)
                    (log/info "created (ms)" created-ms)
                    (log/info "created (ms)" (.toString created))
                    (log/info "entities" entity-count)
                    (log/info "rooms"    rooms-count)
                    (log/info "scales"   scales-count)
                    (log/info "bounds"   bounds)
                    (log/info "width" width)
                    (log/info "height" height)
                    )
                (catch Throwable e
                    (log/error e)
                    (.close fin)))
;           (log/info "x-offset" x-offset)
;           (log/info "y-offset" y-offset)
;           (log/info "scale:" scale)
;           (log/info "scale-info:" scale-info)
;           (log/info "entities:" (count entities))
;           (log/info "rooms" (count rooms))
;           (log/info "selected" selected)
;           (log/info "coordsx" coordsx)
;           (log/info "coordsy" coordsy)
;           (log/info "debug" debug)
            (let [start-time (System/currentTimeMillis)]
                (setup-graphics-context image gc width height)
                (log/info "gc:" gc)
                ;(draw-entities gc entities scale x-offset y-offset user-x-offset user-y-offset)
                ;(draw-rooms gc rooms scale x-offset y-offset user-x-offset user-y-offset selected room-colors coordsx coordsy)
                ;(if debug
                ;    (draw-selection-point gc coordsx coordsy))
                ;    (log/info "Rasterization time (ms):" (- (System/currentTimeMillis) start-time))
                )
        )
    ))

(defn draw-into-image
    [image drawing-id drawing-name width height user-x-offset user-y-offset user-scale
     selected room-colors coordsx coordsy debug]
    (let [full-name  (drawing-full-name drawing-id drawing-name)]
        (if full-name
        (let [data       (read-drawing-from-json full-name)
              scale-info (get-scale data width height)
              x-offset   (:xoffset scale-info)
              y-offset   (:yoffset scale-info)
              scale      (* (:scale scale-info) user-scale)
              entities   (:entities data)
              rooms      (:rooms data)
              gc         (.createGraphics image)]
            (log/info "full drawing name:" full-name)
            (log/info "width" width)
            (log/info "height" height)
            (log/info "x-offset" x-offset)
            (log/info "y-offset" y-offset)
            (log/info "scale:" scale)
            (log/info "scale-info:" scale-info)
            (log/info "entities:" (count entities))
            (log/info "rooms" (count rooms))
            (log/info "selected" selected)
            (log/info "coordsx" coordsx)
            (log/info "coordsy" coordsy)
            (log/info "debug" debug)
            (let [start-time (System/currentTimeMillis)]
                (setup-graphics-context image gc width height)
                (log/info "gc:" gc)
                (draw-entities gc entities scale x-offset y-offset user-x-offset user-y-offset)
                (draw-rooms gc rooms scale x-offset y-offset user-x-offset user-y-offset selected room-colors coordsx coordsy)
                (if debug
                    (draw-selection-point gc coordsx coordsy))
                    (log/info "Rasterization time (ms):" (- (System/currentTimeMillis) start-time)))
        )
    )))

;;;{:capacity 0, :occupied_by , :area 15.0
;;;
;;;        boolean selectArea         = "area".equals(configuration.selectType);
;;;        boolean selectCapacity     = "capacity".equals(configuration.selectType);
;;;        boolean selectOwner        = "owner".equals(configuration.selectType);
;;;        boolean selectAvailability = "availability".equals(configuration.selectType);
;;;        boolean selectPozadavek    = "pozadavek".equals(configuration.selectType);                             ]
;;;


(def occupation-colors
    {"I" {:foreground (new Color 200 100 100)
          :background (new Color 200 100 100 127)}
     "E" {:foreground (new Color 100 100 200)
          :background (new Color 100 100 200 127)}
    })


(def room-type-colors
    {1 {:foreground (new Color 200 150 100)
        :background (new Color 200 150 100 127)}
     2 {:foreground (new Color 100 150 200)
        :background (new Color 100 150 200 127)}
     3 {:foreground (new Color 200 140 200)
        :background (new Color 200 140 200 127)}
     4 {:foreground (new Color 100 200 200)
        :background (new Color 100 200 200 127)}
     5 {:foreground (new Color 200 200 100)
        :background (new Color 200 200 100 127)}
    })


(defn color-for-room-capacity
    [capacity]
    (cond (zero? capacity) {:foreground Color/BLACK :background (new Color 50 50 50 127)}
          (== capacity 1)  {:foreground Color/GRAY :background (new Color 100 100 100 127)}
          (== capacity 2)  {:foreground Color/GRAY :background (new Color 150 150 150 127)}
          :else            {:foreground Color/GRAY :background (new Color 200 200 200 127)}))


(defn compute-room-color
    [highlight-groups room]
    (if highlight-groups
        (or (and (contains? highlight-groups "occupation") (get occupation-colors (:occupation room)))
            (and (contains? highlight-groups "room-type")  (get room-type-colors  (:room_type room)))
            (and (contains? highlight-groups "capacity") (color-for-room-capacity (:capacity room)))
    )))


(defn compute-room-colors
    [floor-id version highlight-groups]
    (let [rooms (db-interface/read-sap-room-list floor-id version)]
         (zipmap (map #(:aoid %) rooms)
                 (map #(compute-room-color highlight-groups %) rooms))))


(defn perform-raster-drawing
    [request]
    (let [params              (:params request)
          floor-id            (get params "floor-id")
          version             (get params "version")
          drawing-id          (get params "drawing-id")
          drawing-name        (get params "drawing-name")
          width               (get params "width" 800)
          height              (get params "height" 600)
          user-x-offset       (utils/parse-int (get params "x-offset" "0"))
          user-y-offset       (utils/parse-int (get params "y-offset" "0"))
          user-scale          (utils/parse-float (get params "scale" "1.0"))
          selected            (get params "selected")
          highlight-p         (get params "highlight")
          highlight-groups    (into #{} (if highlight-p (str/split highlight-p #",")))
          coordsx             (get params "coordsx")
          coordsy             (get params "coordsy")
          coordsx-f           (if coordsx (Double/parseDouble coordsx))
          coordsy-f           (if coordsx (Double/parseDouble coordsy))
          debug               (get params "debug" nil)
          image               (new BufferedImage width height BufferedImage/TYPE_INT_RGB)
          room-colors         (compute-room-colors floor-id version highlight-groups)
          image-output-stream (ByteArrayOutputStream.)]
          (try
              (draw-into-image image drawing-id drawing-name
                               width height
                               user-x-offset user-y-offset user-scale
                               selected room-colors coordsx-f coordsy-f
                               debug)
              (catch Exception e
                  (log/error "error during drawing!" e)))
          ; serialize image into output stream
          (ImageIO/write image "png" image-output-stream)
          (new ByteArrayInputStream (.toByteArray image-output-stream))))


(defn raster-drawing
    "REST API handler for the /api/raster-drawing endpoint."
    [request]
    (let [start-time          (System/currentTimeMillis)
          input-stream        (perform-raster-drawing request)
          end-time            (System/currentTimeMillis)]
          (log/info "Rendering time (ms):" (- end-time start-time))
          (log/info "Image size (bytes): " (.available input-stream))
          (http-utils/png-response input-stream)))

