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

(require '[chainring-service.http-utils :as http-utils])
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

(defn cache-control-headers
    [response]
    (-> response
        (assoc-in [:headers "Cache-Control"] ["must-revalidate" "no-cache" "no-store"])
        (assoc-in [:headers "Expires"] "0")
        (assoc-in [:headers "Pragma"] "no-cache")))

(defn png-response
    [image-data]
    (-> image-data
        (http-response/response)
        (http-response/content-type "image/png")
        cache-control-headers))

(defn proper-scale?
    [item width height]
    (and (= (:width item)  width)
         (= (:height item) height)))

(defn get-scale
    [data width height]
    (let [scales (get data :scales)]
        (first (filter #(proper-scale? % width height) scales))))

(defn transform
    [coordinate scale offset]
    (int (* scale (+ coordinate offset))))

(defn setup-graphics-context
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
    [gc entity scale x-offset y-offset]
    (let [x1 (transform (:x1 entity) scale x-offset)
          y1 (transform (:y1 entity) scale y-offset)
          x2 (transform (:x2 entity) scale x-offset)
          y2 (transform (:y2 entity) scale y-offset)]
          (.drawLine gc x1 y1 x2 y2)))

(defn draw-arc
    [gc entity scale x-offset y-offset]
    (let [x (transform (:x entity) scale x-offset)
          y (transform (:y entity) scale y-offset)
          r (int (* scale (:r entity)))
          a1 (:a1 entity)
          a2 (:a2 entity)
          delta (int (- a2 a1))
          extent (if (neg? delta) (+ delta 360) delta)]
          (.drawArc gc (- x r) (- y r) (* r 2) (*  r 2) a1 extent)))

(defn draw-text
    [gc entity scale x-offset y-offset]
    (let [x (transform (:x entity) scale x-offset)
          y (transform (:y entity) scale y-offset)
          t (:text entity)]
          (.drawString gc t x y)))

(defn draw-entities
    [gc entities scale x-offset y-offset]
    (.setColor gc Color/BLACK)
    (doseq [entity entities]
        (condp = (:T entity) 
            "L" (draw-line gc entity scale x-offset y-offset)
            "A" (draw-arc  gc entity scale x-offset y-offset)
            "T" (draw-text gc entity scale x-offset y-offset)
                nil
        )))

(defn perform-raster-drawing
    [request]
    (let [params         (:params request)
          drawing-id     (get params "drawing-id")
          width          (get params "width" 640)
          height         (get params "height" 480)
          selected       (get params "selected")
          image          (new BufferedImage width height BufferedImage/TYPE_INT_RGB)
          image-output-stream (ByteArrayOutputStream.)]
          ; serialize image into output stream
          (ImageIO/write image "png" image-output-stream)
          (let [end-time (System/currentTimeMillis)]
          (new ByteArrayInputStream (.toByteArray image-output-stream)))))

(defn raster-drawing
    "REST API handler for the /api/raster-drawing endpoint."
    [request]
    (let [start-time          (System/currentTimeMillis)
          input-stream        (perform-raster-drawing request)
          end-time            (System/currentTimeMillis)]
          (log/info "Rendering time (ms):" (- end-time start-time))
          (log/info "Image size (bytes): " (.available input-stream))
          (png-response input-stream)))

(defn send-drawing
    [request mime-type extension]
    [request]
    (let [params     (:params request)
          drawing-id (get params "drawing-id")]
          (log/info "Drawing ID:" drawing-id)
          (if drawing-id
              (try
                  (let [drawing-name (format (str "%05d." extension) (Integer/parseInt drawing-id))]
                       (log/info "Drawing name:" drawing-name)
                       (http-utils/return-file "drawings" drawing-name mime-type))
                  (catch Exception e
                      (log/error e))))))


(defn vector-drawing
    "REST API handler for the /api/vector-drawing endpoint."
    [request]
    (send-drawing request "text/plain" "drw"))

(defn vector-drawing-as-json
    "REST API handler for the /api/vector-drawing-as-json endpoint."
    [request]
    (send-drawing request "application/json" "json"))

