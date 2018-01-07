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
        cache-control-headers
        ))

(defn raster-drawing
    "REST API handler for the /api/raster-drawing endpoint."
    [request]
    (let [image (new BufferedImage 256 256 BufferedImage/TYPE_INT_RGB)
          image-output-stream (ByteArrayOutputStream.)]
          ; serialize image into output stream
          (ImageIO/write image "png" image-output-stream)
          (png-response (new ByteArrayInputStream (.toByteArray image-output-stream)))))

