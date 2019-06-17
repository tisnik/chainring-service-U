;
;  (C) Copyright 2019  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns chainring-service.svg-renderer
    "REST API handlers for returning vector drawings in SVG format.

    Author: Pavel Tisnovsky")


(require '[ring.util.response    :as http-response])
(require '[clojure.tools.logging :as log])

(require '[ring.util.response        :as http-response])


(defn send-drawing
    "Send drawing data to the client.
     Accepted parameters:
         response:  response
         mime-type: the MIME type"
    [response mime-type]
    (-> (http-response/response response)
        (http-response/content-type mime-type)
        (http-response/status 200)))


(def x "<?xml version='1.0' encoding='UTF-8' standalone='no'?>
<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 20010904//EN'
'http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd'>
<!-- Created with Chainring -->
<svg  xmlns='http://www.w3.org/2000/svg'
      xmlns:xlink='http://www.w3.org/1999/xlink'>
          <rect x='10' y='10' height='100' width='100'
                    style='stroke:#ff0000; fill: #0000ff'/>
</svg>")

(defn svg-drawing
    "REST API handler for the /api/svg-drawing endpoint."
    [request]
    (let [params       (:params request)
          building-id  (get params "building-id")
          room-id      (get params "room-id")
          response     (with-out-str (print x))]
          (log/info "Building ID:" building-id)
          (log/info "Building ID:" room-id)
    ; sent the vector drawing in a 'SVG' format
    (send-drawing response "image/svg+xml")))
