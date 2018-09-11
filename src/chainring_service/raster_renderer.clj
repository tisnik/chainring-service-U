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

(require '[chainring-service.http-utils       :as http-utils])
(require '[chainring-service.db-interface     :as db-interface])
(require '[chainring-service.drawings-storage :as drawings-storage])
(require '[chainring-service.drawings-cache   :as drawings-cache])


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


(defn get-scale-from-scales
    "Return scale set up for given width and height values."
    [scales width height]
    (first (filter #(proper-scale? % width height) scales)))


(defn get-scale
    "Return scale set up for given width and height values."
    [data width height]
    (get-scale-from-scales (get data :scales) width height))


(defn transform
    "Transform x or y coordinate using provided scale and offset."
    [coordinate scale offset after-offset center]
    ;(int (+ center after-offset (* scale (+ coordinate offset (- center))))))
    (int (+ center after-offset (* scale (+ coordinate offset (- center))))))
    ;(int (+ after-offset (* scale (+ coordinate offset)))))


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
    [gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center]
    (let [x1 (transform (:x1 entity) scale x-offset user-x-offset h-center)
          y1 (transform (:y1 entity) scale y-offset user-y-offset v-center)
          x2 (transform (:x2 entity) scale x-offset user-x-offset h-center)
          y2 (transform (:y2 entity) scale y-offset user-y-offset v-center)]
          (.drawLine gc x1 y1 x2 y2)))


(defn draw-arc
    [gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center]
    (let [x (transform (:x entity) scale x-offset user-x-offset h-center)
          y (transform (:y entity) scale y-offset user-y-offset v-center)
          r (int (* scale (:r entity)))
          a1 (:a1 entity)
          a2 (:a2 entity)
          delta (int (- a2 a1))
          extent (if (neg? delta) (+ delta 360) delta)]
          (.drawArc gc (- x r) (- y r) (* r 2) (*  r 2) a1 extent)))


(defn draw-circle
    [gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center]
    (let [x (transform (:x entity) scale x-offset user-x-offset h-center)
          y (transform (:y entity) scale y-offset user-y-offset v-center)
          r (int (* scale (:r entity)))]
          (.drawOval gc (- x r) (- y r) (* r 2) (*  r 2))))


(defn draw-text
    [gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center]
    (let [x (transform (:x entity) scale x-offset user-x-offset h-center)
          y (transform (:y entity) scale y-offset user-y-offset v-center)
          t (:text entity)]
          (.drawString gc t x y)))


(defn draw-entities
    [gc entities scale x-offset y-offset user-x-offset user-y-offset h-center v-center]
    (.setColor gc Color/BLACK)
    (doseq [entity entities]
        (condp = (:T entity) 
            "L" (draw-line   gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center)
            "C" (draw-circle gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center) 
            "A" (draw-arc    gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center)
            "T" (draw-text   gc entity scale x-offset y-offset user-x-offset user-y-offset h-center v-center)
                nil
        )))

(defn draw-entities-from-binary-file
    [gc fin entity-count scale x-offset y-offset user-x-offset user-y-offset]
    (.setColor gc Color/BLACK)
    (doseq [i (range entity-count)]
        (let [entity (drawings-storage/read-entity-from-binary fin)]
            (condp = (:T entity) 
                "L" (draw-line   gc entity scale x-offset y-offset user-x-offset user-y-offset)
                "C" (draw-circle gc entity scale x-offset y-offset user-x-offset user-y-offset) 
                "A" (draw-arc    gc entity scale x-offset y-offset user-x-offset user-y-offset)
                "T" (draw-text   gc entity scale x-offset y-offset user-x-offset user-y-offset)
                    nil
            ))))


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
    [aoid selected]
    (= aoid selected))


(defn highlighted-room?
    [aoid room-colors]
    (get room-colors aoid nil))


(defn draw-room
    "Draw room that is passed via the 'room' parameter."
    [gc room scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center]
    (let [polygon (:polygon room)
          aoid    (:room_id room)
          xpoints (map first polygon)
          ypoints (map second polygon)
          transformed-xpoints (map #(transform % scale x-offset user-x-offset h-center) xpoints)
          transformed-ypoints (map #(transform % scale y-offset user-y-offset v-center) ypoints)]
          (if (seq xpoints)
              (cond
                  (selected-room? aoid selected)
                      (draw-selected-room gc transformed-xpoints transformed-ypoints)
                  (highlighted-room? aoid room-colors)
                      (draw-highlighted-room gc transformed-xpoints transformed-ypoints aoid room-colors)
                  :else (draw-regular-room gc transformed-xpoints transformed-ypoints)))))


(defn draw-rooms
    "Draw all rooms that are passed via the 'rooms' parameter."
    [gc rooms scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center]
    (.setStroke gc (new BasicStroke 2))
    (doseq [room rooms]
        (draw-room gc room scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center)))


(defn draw-rooms-from-binary
    "Draw rooms, data is read from the binary file."
    [gc fin rooms-count scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center]
    (.setStroke gc (new BasicStroke 2))
    (doseq [i (range rooms-count)]
        (let [room (drawings-storage/read-room-from-binary fin)]
            (draw-room gc room scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center))))


(defn draw-grid
    [gc width height grid-size grid-color]
    (.setColor gc grid-color)
    (.setStroke gc (new BasicStroke 1))
    (doseq [y (range 0 (inc height) grid-size)]
        (doseq [x (range 0 (inc width) grid-size)]
            (.drawLine gc x y x y))))
            ;(.drawLine gc (dec x) y (inc x) y)
            ;(.drawLine gc x (dec y) x (inc y)))))


(defn draw-boundary
    [gc bounds scale x-offset y-offset user-x-offset user-y-offset boundary-color h-center v-center]
    (.setColor gc boundary-color)
    (.setStroke gc (new BasicStroke 1))
    (let [x1 (transform (:xmin bounds) scale x-offset user-x-offset h-center)
          y1 (transform (:ymin bounds) scale y-offset user-y-offset v-center)
          x2 (transform (:xmax bounds) scale x-offset user-x-offset h-center)
          y2 (transform (:ymax bounds) scale y-offset user-y-offset v-center)]
          (.drawRect gc x1 y1 (- x2 x1) (- y2 y1))
          (.drawRect gc (+ x1 2) (+ y1 2) (- x2 x1 4) (- y2 y1 4))))


(defn draw-selection-point
    [gc x y blip-size blip-color]
    (when (and x y)
          (.setStroke gc (new BasicStroke 1))
          (.setColor gc blip-color)
          (.drawLine gc (- x blip-size) y (+ x blip-size) y)
          (.drawLine gc x (- y blip-size) x (+ y blip-size))))


(defn drawing-full-name
    [drawing-id drawing-name]
    (if drawing-id
        (format (str "drawings/%s.json") drawing-id)
        (if drawing-name
            (str "drawings/" drawing-name))))


(defn drawing-full-name-binary
    [drawing-id drawing-name]
    (if drawing-id
        (format (str "drawings/%05d.bin") (Integer/parseInt drawing-id))
        (if drawing-name
            (str "drawings/" drawing-name))))


(defn read-drawing-from-json
    ( [filename]
        (let [start-time (System/currentTimeMillis)
              data       (json/read-str (slurp filename) :key-fn clojure.core/keyword)
              end-time   (System/currentTimeMillis)]
              (log/info "JSON loading time (ms):" (- end-time start-time))
              data))
    ( [filename message]
        (log/info message)
        (read-drawing-from-json filename)))


(defn prepare-data-stream
    [filename]
    (let [fis (new java.io.FileInputStream filename)]
        (new java.io.DataInputStream fis)))

(defn draw-into-image-from-binary-data
    [image drawing-id drawing-name width height user-x-offset user-y-offset user-scale
     selected room-colors coordsx coordsy show-grid show-boundary show-blips
     debug configuration]
    (if-let [full-name        (drawing-full-name-binary drawing-id drawing-name)]
        (let [fin             (prepare-data-stream full-name)
              gc              (.createGraphics image)
              grid-size       (-> configuration :renderer :grid-size)
              grid-rgb        (-> configuration :renderer :grid-color)
              grid-color      (utils/rgb->Color grid-rgb)
              boundary-rgb    (-> configuration :renderer :boundary-color)
              boundary-color  (utils/rgb->Color boundary-rgb)
              blip-size       (-> configuration :renderer :blip-size)
              blip-rgb        (-> configuration :renderer :blip-color)
              blip-color      (utils/rgb->Color blip-rgb)]
            (try
                (let [[magic-number file-version data-version] (drawings-storage/read-binary-header fin)
                      [created-ms created]                     (drawings-storage/read-created-time fin)
                      [entity-count rooms-count scales-count]  (drawings-storage/read-counters fin)
                      bounds                                   (drawings-storage/read-bounds fin)
                      scales                                   (drawings-storage/read-scales fin scales-count)
                      scale-info (get-scale-from-scales scales width height)
                      x-offset   (:xoffset scale-info)
                      y-offset   (:yoffset scale-info)
                      scale      (* (:scale scale-info) user-scale)]
                    (assert (= magic-number 0x6502))
                    (assert (= file-version 1))
                    (assert (= data-version 1))
                    (when debug
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
                          (log/info "x-offset" x-offset)
                          (log/info "y-offset" y-offset)
                          (log/info "scale:" scale)
                          (log/info "scale-info:" scale-info)
                          (log/info "width" width)
                          (log/info "height" height)
                          (doseq [scale scales]
                              (log/info "scale" scale)))
                    (let [start-time (System/currentTimeMillis)]
                        (setup-graphics-context image gc width height)
                        (log/info "gc:" gc)
                        (draw-entities-from-binary-file gc fin entity-count scale x-offset y-offset user-x-offset user-y-offset)
                        (draw-rooms-from-binary gc fin rooms-count scale x-offset y-offset user-x-offset user-y-offset selected room-colors)
                        (if (or debug show-blips)
                            (draw-selection-point gc coordsx coordsy blip-size blip-color))
                        (log/info "Rasterization time (ms):" (- (System/currentTimeMillis) start-time))
                    ))
                (catch Throwable e
                    (log/error e)
                    (.close fin)))
        )
    ))

(defn get-drawing-data
    [drawing-id drawing-name use-memory-cache]
    (when-let [full-name (drawing-full-name drawing-id drawing-name)]
        (log/info "full drawing name:" full-name)
        (if (and drawing-id use-memory-cache)
            (let [data (or (drawings-cache/fetch drawing-id) (read-drawing-from-json full-name "Cache miss, must read JSON"))]
                 (drawings-cache/store drawing-id data)
                 data)
            (read-drawing-from-json full-name "Forced read from JSON"))))


(defn offset+scale
    [data width height user-scale]
    (let [scale-info (get-scale data width height)
          x-offset   (:xoffset scale-info)
          y-offset   (:yoffset scale-info)
          scale      (* (:scale scale-info) user-scale)]
          [x-offset y-offset scale]))


(defn draw-into-image
    [image drawing-id drawing-name width height user-x-offset user-y-offset user-scale
     selected room-colors coordsx coordsy use-memory-cache
     show-grid show-boundary show-blips
     debug configuration]
    (let [data (get-drawing-data drawing-id drawing-name use-memory-cache)]
        (if data
        (let [[x-offset y-offset scale] (offset+scale data width height user-scale)
              h-center        (/ width 2)
              v-center        (/ height 2)
              entities        (:entities data)
              rooms           (:rooms data)
              bounds          (:bounds data)
              gc              (.createGraphics image)
              grid-size       (-> configuration :renderer :grid-size)
              grid-rgb        (-> configuration :renderer :grid-color)
              grid-color      (utils/rgb->Color grid-rgb)
              boundary-rgb    (-> configuration :renderer :boundary-color)
              boundary-color  (utils/rgb->Color boundary-rgb)
              blip-size       (-> configuration :renderer :blip-size)
              blip-rgb        (-> configuration :renderer :blip-color)
              blip-color      (utils/rgb->Color blip-rgb)]
            (log/info "width x height" width height)
            (log/info "offset" x-offset y-offset)
            (log/info "scale:" scale)
            (log/info "entities:" (count entities))
            (log/info "rooms" (count rooms))
            (log/info "bounds" bounds)
            (log/info "selected" selected)
            (log/info "clicked" coordsx coordsy)
            (log/info "grid" show-grid grid-size grid-color)
            (log/info "boundary" show-boundary)
            (log/info "blip" show-blips blip-size blip-rgb)
            (log/info "debug" debug)
            (let [start-time (System/currentTimeMillis)]
                (setup-graphics-context image gc width height)
                (log/info "gc:" gc)
                (if show-grid
                    (draw-grid gc width height grid-size grid-color))
                (draw-entities gc entities scale x-offset y-offset user-x-offset user-y-offset h-center v-center)
                (draw-rooms gc rooms scale x-offset y-offset user-x-offset user-y-offset selected room-colors h-center v-center)
                (if show-boundary
                    (draw-boundary gc bounds scale x-offset y-offset user-x-offset user-y-offset boundary-color h-center v-center))
                (if (or debug show-blips)
                    (draw-selection-point gc coordsx coordsy blip-size blip-color))
                (log/info "Rasterization time (ms):" (- (System/currentTimeMillis) start-time)))
        )
    )))

(defn aoid+selected
    [rooms scale x-offset user-x-offset y-offset user-y-offset
     coordsx coordsy h-center v-center]
     (for [room rooms]
         (let [polygon (:polygon room)
               aoid    (:room_id room)
               xpoints (map first polygon)
               ypoints (map second polygon)
               transformed-xpoints (map #(transform % scale x-offset user-x-offset h-center) xpoints)
               transformed-ypoints (map #(transform % scale y-offset user-y-offset v-center) ypoints)]
               (if (seq xpoints)
                   {:aoid aoid
                    :selected (coords-in-polygon transformed-xpoints transformed-ypoints coordsx coordsy)}
                   {:aoid aoid
                    :selected nil}))))

(defn find-room
    [drawing-id drawing-name width height
     user-x-offset user-y-offset user-scale coordsx coordsy use-memory-cache
     h-center v-center]
    (let [data (get-drawing-data drawing-id drawing-name use-memory-cache)]
        (if data
            (let [[x-offset y-offset scale] (offset+scale data width height user-scale)
                  rooms      (:rooms data)
                  aoids      (aoid+selected rooms scale x-offset user-x-offset y-offset user-y-offset coordsx coordsy h-center v-center)]
                  ; output value
                  (->> aoids
                      (filter #(:selected %))
                      first
                      :aoid)))))

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
            (and (contains? highlight-groups "room_type")  (get room-type-colors  (:room_type room)))
            (and (contains? highlight-groups "capacity") (color-for-room-capacity (:capacity room)))
    )))


(defn compute-room-colors
    [floor-id version highlight-groups]
    (let [rooms (db-interface/read-sap-room-list floor-id version)]
         (zipmap (map #(:id %) rooms)
                 (map #(compute-room-color highlight-groups %) rooms))))


(defn use-binary-rendering?
    [use-binary? drawing-name]
    ; if drawing-name is set, use this name to decide
    ; otherwise use the settings 'use-binary?'
    (if drawing-name
        (.endsWith drawing-name ".bin")
        use-binary?))


(defn perform-raster-drawing
    [request]
    (let [params              (:params request)
          configuration       (:configuration request)
          use-binary?         (-> configuration :drawings :use-binary)
          use-memory-cache    (-> configuration :drawings :use-memory-cache)
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
          ; make sure that only 'true' is converted into truth value, nil/false otherwise
          show-grid           (-> (get params "grid" "false")     utils/parse-boolean)
          show-boundary       (-> (get params "boundary" "false") utils/parse-boolean)
          show-blips          (-> (get params "blip" "false")     utils/parse-boolean)
          coordsx-f           (if coordsx (Double/parseDouble coordsx))
          coordsy-f           (if coordsx (Double/parseDouble coordsy))
          debug               (get params "debug" nil)
          image               (new BufferedImage width height BufferedImage/TYPE_INT_RGB)
          room-colors         (compute-room-colors floor-id version highlight-groups)
          image-output-stream (ByteArrayOutputStream.)]
          (try
              (if (use-binary-rendering? use-binary? drawing-name)
                  (draw-into-image-from-binary-data image drawing-id drawing-name
                                   width height
                                   user-x-offset user-y-offset user-scale
                                   selected room-colors coordsx-f coordsy-f
                                   show-grid show-boundary show-blips
                                   debug configuration)
                  (draw-into-image image drawing-id drawing-name
                                   width height
                                   user-x-offset user-y-offset user-scale
                                   selected room-colors coordsx-f coordsy-f
                                   use-memory-cache
                                   show-grid show-boundary show-blips
                                   debug configuration))
              (catch Exception e
                  (log/error "error during drawing!" e)))
          ; serialize image into output stream
          (ImageIO/write image "png" image-output-stream)
          (new ByteArrayInputStream (.toByteArray image-output-stream))))


(defn perform-find-room
    [request]
    (let [params              (:params request)
          configuration       (:configuration request)
          use-binary?         (-> configuration :drawings :use-binary)
          use-memory-cache    (-> configuration :drawings :use-memory-cache)
          floor-id            (get params "floor-id")
          version             (get params "version")
          drawing-id          (get params "drawing-id")
          drawing-name        (get params "drawing-name")
          width               (get params "width" 800)
          height              (get params "height" 600)
          h-center            (/ width 2)
          v-center            (/ height 2)
          user-x-offset       (utils/parse-int (get params "x-offset" "0"))
          user-y-offset       (utils/parse-int (get params "y-offset" "0"))
          user-scale          (utils/parse-float (get params "scale" "1.0"))
          coordsx             (get params "coordsx")
          coordsy             (get params "coordsy")
          coordsx-f           (if coordsx (Double/parseDouble coordsx))
          coordsy-f           (if coordsx (Double/parseDouble coordsy))]
          (try
              (if (use-binary-rendering? use-binary? drawing-name)
                  nil
                 ;(find-room-from-binary-data image drawing-id drawing-name
                 ;                 width height
                 ;                 user-x-offset user-y-offset user-scale
                 ;                 selected room-colors coordsx-f coordsy-f
                 ;                 debug)
                  (find-room drawing-id drawing-name
                             width height
                             user-x-offset user-y-offset user-scale
                             coordsx-f coordsy-f use-memory-cache h-center v-center))
              (catch Exception e
                  (log/error "error during finding room!" e)))))

(defn raster-drawing
    "REST API handler for the /api/raster-drawing endpoint."
    [request]
    (let [start-time          (System/currentTimeMillis)
          input-stream        (perform-raster-drawing request)
          end-time            (System/currentTimeMillis)]
          (log/info "Rendering time (ms):" (- end-time start-time))
          (log/info "Image size (bytes): " (.available input-stream))
          (http-utils/png-response input-stream)))

(defn find-room-on-drawing
    [request]
    (let [start-time          (System/currentTimeMillis)
          room                (perform-find-room request)
          end-time            (System/currentTimeMillis)]
          (log/info "Finding time (ms):" (- end-time start-time))
          (-> (http-response/response room)
              (http-response/content-type "text/plain")
              (http-response/status 200))))
