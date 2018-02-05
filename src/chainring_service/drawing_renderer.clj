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

(ns chainring-service.drawing-renderer
    "Namespace that contains functions to render drawings into raster images.")

(require '[ring.util.response :as http-response])
(require '[clojure.tools.logging   :as log])

(require '[chainring-service.http-utils  :as http-utils])

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

