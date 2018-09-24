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

(ns chainring-service.mocked-sap-interface)

(require '[clojure.tools.logging        :as log])
(require '[chainring-service.csv-loader :as csv-loader])

(def data-directory
    "data")

(def dates-from
    ["2000-01-01"
     "2018-01-01"
     "2018-07-01"
     "2018-09-01"])

(def areals-data-file-name
    "areals.csv")

(def buildings-data-file-name
    "buildings.csv")

(def floors-data-file-name
    "floors.csv")

(def rooms-data-file-name
    "rooms.csv")

(def room-attribute-types-file-name
    "attribute_types.csv")

(def room-attributes-file-name
    "room_attributes.csv")

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

(defn load-all-data-files
    []
    (reset! areals               (csv-loader/load-csv-for-all-dates dates-from data-directory areals-data-file-name))
    (reset! buildings            (csv-loader/load-csv-for-all-dates dates-from data-directory buildings-data-file-name))
    (reset! floors               (csv-loader/load-csv-for-all-dates dates-from data-directory floors-data-file-name))
    (reset! rooms                (csv-loader/load-csv-for-all-dates dates-from data-directory rooms-data-file-name))
    (reset! room-attribute-types (csv-loader/load-csv (str data-directory "/" room-attribute-types-file-name)))
    (reset! room-attributes      (csv-loader/load-csv (str data-directory "/" room-attributes-file-name)))
)

(load-all-data-files)

(defn aoids-count-per-date
    [dates-from aoids]
    (zipmap dates-from
            (for [date dates-from] (count (get aoids date)))))

(defn reload-mock-data
    []
    (log/info "Reload mock data begin")
    (log/info "Dates: " dates-from)
    (load-all-data-files)
    (let [status {:dates-from dates-from
                  :areals     (aoids-count-per-date dates-from @areals)
                  :buildings  (aoids-count-per-date dates-from @buildings)
                  :floors     (aoids-count-per-date dates-from @floors)
                  :rooms      (aoids-count-per-date dates-from @rooms)}]
        (log/info "Reload mock data end")
        status))

(defn get-real-date-from
    "Retrieve the date from dates-from list that is closely older than given date."
    [valid-from]
    (if (not valid-from)
        (last dates-from)
        (->> dates-from
             (filter #(not (neg? (compare valid-from %))))
             last)))


(defn read-all-dates-from
    []
    dates-from)


(defn read-areals
    [valid-from]
    (let [real-date-from (get-real-date-from valid-from)]
        {:date-from real-date-from
         :areals    (get @areals real-date-from)}))


(defn read-areal-info
    [areal valid-from]
    (let [real-date-from (get-real-date-from valid-from)]
        (first (filter #(= areal (:AOID %)) (get @areals real-date-from)))))


(defn read-buildings
    [areal valid-from]
    (let [real-date-from     (get-real-date-from valid-from)
          buildings-for-date (get @buildings real-date-from)]
        (if areal
            (let [prefix (str areal ".")]
                (filter #(.startsWith (:AOID %) prefix) buildings-for-date))
            buildings-for-date)))


(defn read-building-info
    [areal building valid-from]
    (let [real-date-from     (get-real-date-from valid-from)
          buildings-for-date (get @buildings real-date-from)]
        (if building
            (first (filter #(= building (:AOID %)) buildings-for-date)))))


(defn read-floors
    [areal building valid-from]
    (let [real-date-from  (get-real-date-from valid-from)
          floors-for-date (get @floors real-date-from)]
        (if areal
            (if building
                ; filter by areal-id and building-id
                (let [prefix (str building ".")]
                    (filter #(.startsWith (:AOID %) prefix) floors-for-date))
                ; filter by areal-id
                (let [prefix (str areal ".")]
                    (filter #(.startsWith (:AOID %) prefix) floors-for-date)))
            (if building
                ; filter by building-id
                (let [prefix (str building ".")]
                    (filter #(.startsWith (:AOID %) prefix) floors-for-date))
                ; no filtering at all
                floors-for-date))))


(defn read-floor-info
    [areal building floor valid-from]
    (let [real-date-from  (get-real-date-from valid-from)
          floors-for-date (get @floors real-date-from)]
        (if floor
            (first (filter #(= floor (:AOID %)) floors-for-date))
            floors-for-date)))


(defn read-rooms
    [floor valid-from]
    (let [real-date-from (get-real-date-from valid-from)
          rooms-for-date (get @rooms real-date-from)]
       (if floor
           (let [prefix (str floor ".")]
                (filter #(.startsWith (:AOID %) prefix) rooms-for-date))
           rooms-for-date)))


(defn read-room-info
    [room valid-from]
    (let [real-date-from (get-real-date-from valid-from)
          rooms-for-date (get @rooms real-date-from)]
       (if room
            (first (filter #(= room (:AOID %)) rooms-for-date))
            rooms-for-date)))


(defn read-room-attribute-types
    []
    @room-attribute-types)


(defn read-rooms-attribute
    [floor valid-from attribute-name]
    (let [ra @room-attributes
          selector (keyword attribute-name)]
        (for [room ra] {:AOID (:Room room)
                        :Label attribute-name
                        :key (get room selector)
                        :value (get room selector)})))
