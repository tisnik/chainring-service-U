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
(require '[clojure.data.json     :as json])

(require '[chainring-service.drawing-utils :as drawing-utils])


(defn send-drawing
    "Send drawing data to the client.
     Accepted parameters:
         response:  response
         mime-type: the MIME type"
    [response mime-type]
    (-> (http-response/response response)
        (http-response/content-type mime-type)
        (http-response/status 200)))


(defn render-empty-svg
    []
    (with-out-str
        (println "<?xml version='1.0' encoding='UTF-8' standalone='no'?>
    <!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 20010904//EN'
    'http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd'>
    <!-- Created with Chainring -->
    <svg  xmlns='http://www.w3.org/2000/svg'
          xmlns:xlink='http://www.w3.org/1999/xlink' style='font-size:1em'>
          <image xlink:href='/bglogo.png' width='500' height='500' />
          <rect x='-50' y='-50' width='600' height='600' style='fill:none;stroke:none' />
    </svg>")))


(defn drawing-full-name
    "Construct filename with drawing from either drawing id or drawing name."
    [drawing-name]
    (if drawing-name
        (str "drawings/" drawing-name)))


(defn read-drawing-from-json
    "Read drawing data from the JSON file."
    ( [filename]
        (let [start-time (System/currentTimeMillis)
              data       (json/read-str (slurp filename) :key-fn clojure.core/keyword)
              end-time   (System/currentTimeMillis)]
              (log/info "JSON loading time (ms):" (- end-time start-time))
              data))
    ( [filename message]
        (log/info message)
        (read-drawing-from-json filename)))


(defn draw-svg-header
    []
    (println "<?xml version='1.0' encoding='UTF-8' standalone='no'?>
<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 20010904//EN'
'http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd'>
<!-- Created with Chainring -->
<svg  xmlns='http://www.w3.org/2000/svg'
      xmlns:xlink='http://www.w3.org/1999/xlink' style='font-size:1em'>"))


(defn draw-svg-footer
    []
    (println "</svg>"))


(defn draw-line
    "Draw a line entity"
    [entity]
    (let [x1 (:x1 entity)
          y1 (:y1 entity)
          x2 (:x2 entity)
          y2 (:y2 entity)]
          (println (format "<line x1='%f' y1='%f' x2='%f' y2='%f' style='stroke:rgb(0,0,0)' />", x1, y1, x2, y2))))


(defn deg->rad
    [angle-in-degrees]
    (* angle-in-degrees (/ Math/PI 180.0)))


(defn polar-to-cartesian-x
    [center-x radius angle-in-degrees]
    (+ center-x (* radius (Math/cos (deg->rad angle-in-degrees)))))


(defn polar-to-cartesian-y
    [center-y radius angle-in-degrees]
    (- center-y (* radius (Math/sin (deg->rad angle-in-degrees)))))


(defn draw-arc
    "Draw an arc entity."
    [entity]
    (let [x (:x entity)
          y (:y entity)
          r (:r entity)
          a1 (:a1 entity)
          a2 (:a2 entity)
          delta (int (- a2 a1))
          extent (if (neg? delta) (+ delta 360) delta)
          start-x (polar-to-cartesian-x x r a1)
          start-y (polar-to-cartesian-y y r a1)
          end-x   (polar-to-cartesian-x x r a2)
          end-y   (polar-to-cartesian-y y r a2)
          large-arc (if (<= (- a2 a1) 180) "0" "1")]
          ; debug
          ;(println (format "<circle cx='%f' cy='%f' r='%f' style='stroke:rgb(0,0,255)' fill='none' />" x y r))
          ;(println (format "<line x1='%f' y1='%f' x2='%f' y2='%f' style='stroke:rgb(255,0,0)' />" start-x start-y end-x end-y))
          ; debug
          (println (format "<path d='M %f %f A %f %f 0 %s 0 %f %f' style='stroke:rgb(255,0,0)' fill='none' />",
               start-x start-y r r large-arc end-x end-y))
          ))


(defn draw-circle
    "Draw a circle entity."
    [entity]
    (let [x (:x entity)
          y (:y entity)
          r (:r entity)]
          (println (format "<circle cx='%f' cy='%f' r='%f' style='stroke:rgb(0,0,255)' fill='none' />", x, y, r))))


(defn draw-text
    "Draw a text entity."
    [entity]
    (let [x (:x entity)
          y (:y entity)
          t (:text entity)]
          (println (format "<text x='%f' y='%f' font-family='Arial, Helvetica, sans-serif'>%s</text>", x, y, t))))


(defn draw-entities
    [entities]
    (doseq [entity entities]
        (if (not= "koty" (clojure.string/lower-case (str (:layer entity))))
            (condp = (:T entity) 
                "L" (draw-line   entity)
                "C" (draw-circle entity) 
                "A" (draw-arc    entity)
                "T" (draw-text   entity)
                    nil
        ))))


(defn selected-room?
    "Check if the room was selected by user."
    [aoid selected]
    (= aoid selected))


(defn draw-room-contour
    "Draw contour of highlighted room."
    [points style]
    (let [polygon (clojure.string/join " " points)]
        (println (format "<polygon points='%s' style='%s' />" polygon style))))


(defn draw-selected-room
    "Draw background and contour of selected room."
    [points]
    (draw-room-contour points "fill:yellow;stroke:red;stroke-width:2"))


(defn draw-regular-room
    "Draw regular room onto the canvas."
    [points]
    (draw-room-contour points "fill:none;stroke:blue;stroke-width:2"))


(defn draw-room
    [room selected]
    (let [polygon (:polygon room)
          aoid    (:room_id room)
          points  (map #(clojure.string/join "," %) polygon)]
          (if (seq points)
              (if (selected-room? aoid selected)
                  (draw-selected-room points)
                  (draw-regular-room  points)))))


(defn draw-rooms
    [rooms selected]
    (doseq [room rooms]
        (draw-room room selected)))


(defn render-svg-drawing-from
    [drawing-name room-id]
    (log/info "Drawing name:" drawing-name)
    (let [data (read-drawing-from-json (drawing-full-name drawing-name))]
        (if data
            (let [entities  (:entities data)
                  rooms     (:rooms data)
                  bounds    (:bounds data)]
                (with-out-str
                    (draw-svg-header)
                    (draw-rooms rooms room-id)
                    (draw-entities entities)
                    (draw-svg-footer)
                ))
            (render-empty-svg))))



(defn render-svg-drawing
    [building-id floor-id room-id]
    (log/info "Building ID:" building-id)
    (log/info "Floor ID:" floor-id)
    (log/info "Room ID:" room-id)
    (let [drawing-name (drawing-utils/read-latest-drawing-for-floor floor-id)]
        (if drawing-name
            (render-svg-drawing-from drawing-name room-id)
            (render-empty-svg))))


(defn svg-drawing
    "REST API handler for the /api/svg-drawing endpoint."
    [request]
    (let [params       (:params request)
          building-id  (get params "building-id")
          floor-id     (get params "floor-id")
          room-id      (get params "room-id")
          response     (if (and building-id floor-id room-id)
                           (render-svg-drawing building-id floor-id room-id)
                           (render-empty-svg))]
    ; sent the vector drawing in a 'SVG' format
    (send-drawing response "image/svg+xml")))
