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

(ns chainring-service.real-sap-interface)

(require '[clojure.tools.logging        :as log])
(require '[chainring-service.csv-loader :as csv-loader])

(def areals
    (atom nil))

(def buildings
    (atom nil))

(def floors
    (atom nil))

(def rooms
    (atom nil))

(def room-attribute-types
    (atom nil))

(def room-attributes
    (atom nil))

(def last-update
    (atom nil))

(defn read-areals
    []
    [1 2])

(defn read-buildings
    [areal]
    [1 2 3])

(defn read-floors
    [areal building]
    [1 2 3 4])

(defn read-rooms
    [areal building floor]
    [1 2 3 4 5])

(defn read-building-info
    [building-id]
    [1 2])

(defn minutes-to-seconds
    "Converts minutes to seconds."
    [minutes]
    (* minutes 60))

(defn seconds-to-ms
    "Converts seconds to milliseconds."
    [seconds]
    (* seconds 1000))

(defn compute-sleep-amount
    [minutes]
    (-> minutes
        minutes-to-seconds
        seconds-to-ms))

(defn load-all-data-files
    []
    (reset! areals               (csv-loader/load-csv "data/2018-09-01/areals.csv"))
    (reset! buildings            (csv-loader/load-csv "data/2018-09-01/buildings.csv"))
    (reset! floors               (csv-loader/load-csv "data/2018-09-01/floors.csv"))
    (reset! rooms                (csv-loader/load-csv "data/2018-09-01/rooms.csv"))
    (reset! room-attribute-types (csv-loader/load-csv "data/attribute_types.csv"))
    (reset! room-attributes      (csv-loader/load-csv "data/room_attributes.csv"))
)

(println "********************")
(load-all-data-files)
(println "********************")

(defn run-fetcher-in-a-loop
    "Run the fetcher periodically. The sleep amount should containg time delay in minutes."
    [sleep-amount]
    (let [ms-to-sleep (compute-sleep-amount sleep-amount)]
        (while true
            (do
                (log/info "SAP fetcher started")
                (load-all-data-files)
                (reset! last-update (new java.util.Date))
                (log/info (str "SAP fetcher finished, sleeping for " sleep-amount " minutes"))
                (Thread/sleep ms-to-sleep)))))


(defn run-fetcher
    "Run the endless fetcher loop."
    []
    (log/info "SAP fetcher started in its own thread")
    (run-fetcher-in-a-loop 1))


(defn today?
    [valid-from]
    (if valid-from
        (let [timeformatter (new java.text.SimpleDateFormat "yyyy-MM-dd")
              now           (new java.util.Date)
              now-str       (.format timeformatter now)]
              (= now-str valid-from))
        true))


(defn read-areals
    [valid-from]
    (if (today? valid-from)
        {:date-from valid-from
         :areals    @areals}
        nil))


(defn read-areal-info
    [areal valid-from]
    (if (today? valid-from)
        (first (filter #(= areal (:AOID %)) @areals))
        nil))


(defn read-buildings
    [areal valid-from]
    (if (today? valid-from)
        (if areal
            (let [prefix (str areal ".")]
                (filter #(.startsWith (:AOID %) prefix) @buildings))
            @buildings)
         nil))


(defn read-building-info
    [building valid-from]
    (if (today? valid-from)
        (if building
            (first (filter #(= building (:AOID %)) @buildings)))))


(defn read-floors
    [areal building valid-from]
    (if (today? valid-from)
        (if areal
            (if building
                ; filter by areal-id and building-id
                (let [prefix (str building ".")]
                    (filter #(.startsWith (:AOID %) prefix) @floors))
                ; filter by areal-id
                (let [prefix (str areal ".")]
                    (filter #(.startsWith (:AOID %) prefix) @floors)))
            (if building
                ; filter by building-id
                (let [prefix (str building ".")]
                    (filter #(.startsWith (:AOID %) prefix) @floors))
                ; no filtering at all
                @floors))))


(defn read-floor-info
    [floor valid-from]
    (if (today? valid-from)
        (if floor
            (first (filter #(= floor (:AOID %)) @floors))
            @floors)))


(defn read-rooms
    [floor valid-from]
    (if (today? valid-from)
       (if floor
           (let [prefix (str floor ".")]
                (filter #(.startsWith (:AOID %) prefix) @rooms))
           @rooms)))


(defn read-room-attribute-types
    []
    @room-attribute-types)


(defn read-rooms-attribute
    [floor valid-from attribute-name]
    (let [ra @room-attributes
          selector (keyword attribute-name)]
        (zipmap (for [room ra] (:Room room))
                (for [room ra] (get room selector)))))
