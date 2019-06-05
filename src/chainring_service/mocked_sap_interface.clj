;
;  (C) Copyright 2017, 2018, 2019  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns chainring-service.mocked-sap-interface
    "Implementation of mocks for the SAP interface.

    Author: Pavel Tisnovsky")


(require '[clojure.tools.logging        :as log])
(require '[chainring-service.csv-loader :as csv-loader])


(def data-directory
    "data")


(def dates-from
    ["2000-01-01"
     "2018-01-01"
     "2018-07-01"
     "2018-09-01"])


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
    "Load all files with mock data."
    []
    (reset! buildings            (csv-loader/load-csv-for-all-dates dates-from data-directory buildings-data-file-name))
    (reset! floors               (csv-loader/load-csv-for-all-dates dates-from data-directory floors-data-file-name))
    (reset! rooms                (csv-loader/load-csv-for-all-dates dates-from data-directory rooms-data-file-name))
    (reset! room-attribute-types (csv-loader/load-csv (str data-directory "/" room-attribute-types-file-name)))
    (reset! room-attributes      (csv-loader/load-csv (str data-directory "/" room-attributes-file-name)))
)


(load-all-data-files)


(defn aoids-count-per-date
    "Provide a map with dates mapped to AOIDs."
    [dates-from aoids]
    (zipmap dates-from
            (for [date dates-from] (count (get aoids date)))))


(defn reload-mock-data
    "Reload all files with mock data."
    []
    (log/info "Reload mock data begin")
    (log/info "Dates: " dates-from)
    (load-all-data-files)
    (let [status {:dates-from dates-from
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
    "Read set of all possible dates-from values."
    []
    dates-from)


(defn read-buildings
    "Read list of buildings."
    [valid-from]
    (let [real-date-from     (get-real-date-from valid-from)
          buildings-for-date (get @buildings real-date-from)]
            buildings-for-date))


(defn read-building-info
    "Read building info for given AOID."
    [building valid-from]
    (let [real-date-from     (get-real-date-from valid-from)
          buildings-for-date (get @buildings real-date-from)]
        (if building
            (first (filter #(= building (:AOID %)) buildings-for-date)))))


(defn read-floors
    "Read list of floors for selected building."
    [building valid-from]
    (let [real-date-from  (get-real-date-from valid-from)
          floors-for-date (get @floors real-date-from)]
        (if building
            ; filter by building-id
            (let [prefix (str building ".")]
                (filter #(.startsWith (:AOID %) prefix) floors-for-date))
            ; no filtering at all
            floors-for-date)))


(defn read-floor-info
    "Read floor info for given AOID."
    [building floor valid-from]
    (let [real-date-from  (get-real-date-from valid-from)
          floors-for-date (get @floors real-date-from)]
        (if floor
            (first (filter #(= floor (:AOID %)) floors-for-date))
            floors-for-date)))


(defn read-rooms
    "Read list of rooms for selected floor."
    [floor valid-from]
    (let [real-date-from (get-real-date-from valid-from)
          rooms-for-date (get @rooms real-date-from)]
       (if floor
           (let [prefix (str floor ".")]
                (filter #(.startsWith (:AOID %) prefix) rooms-for-date))
           rooms-for-date)))


(defn read-room-info
    "Read floor info for given AOID."
    [room valid-from]
    (let [real-date-from (get-real-date-from valid-from)
          rooms-for-date (get @rooms real-date-from)]
       (if room
            (first (filter #(= room (:AOID %)) rooms-for-date))
            rooms-for-date)))


(defn read-room-attribute-types
    "Read all possible room attributes."
    []
    @room-attribute-types)


(defn read-rooms-attribute
    "Read selected attribute values for all rooms from floor."
    [floor valid-from attribute-name]
    (let [ra @room-attributes
          selector (keyword attribute-name)]
        (for [room ra] {:AOID (:Room room)
                        :Label attribute-name
                        :key (get room selector)
                        :value (get room :ozn)})))


(defn read-rooms-possible-attributes
    "Read set of possible attribute values for all rooms from floor."
    [floor-id valid-from attribute-id]
    [
"sklad",
"WC a sprcha"
"chodba"
"schodiště"
"kancelář"
"ostatní"
"rozvodna"
"šatna"
"archiv"
"jídelna"
"kuchyňka"])
