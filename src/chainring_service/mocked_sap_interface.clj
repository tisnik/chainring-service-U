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

(require '[clojure.data.csv :as csv])
(require '[clojure.java.io  :as io])

(require '[clojure.tools.logging      :as log])

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

(def areals
    (atom nil))

(def buildings
    (atom nil))

(def floors
    (atom nil))

(def rooms
    (atom nil))

(defn csv-data->maps
    [csv-data]
    (map zipmap
        (->> (first csv-data)  ;; header
             (map keyword)     ;; heder items -> keywords
             repeat)
             (rest csv-data)))

(defn load-csv
    [filename]
    (with-open [reader (io/reader filename)]
        (let [data (csv/read-csv reader)]
             (doall (csv-data->maps data)))))

(defn load-csv-for-all-dates
    [dates-from data-directory filename]
    (zipmap dates-from
        (for [date dates-from]
            (let [full-filename (str data-directory "/" date "/" filename)]
                (load-csv full-filename)))))

(defn load-all-data-files
    []
    (reset! areals    (load-csv-for-all-dates dates-from data-directory areals-data-file-name))
    (reset! buildings (load-csv-for-all-dates dates-from data-directory buildings-data-file-name))
    (reset! floors    (load-csv-for-all-dates dates-from data-directory floors-data-file-name))
    (reset! rooms     (load-csv-for-all-dates dates-from data-directory rooms-data-file-name)))

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
    [building valid-from]
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
    [floor valid-from]
    (let [real-date-from  (get-real-date-from valid-from)
          floors-for-date (get @floors real-date-from)]
        (if floor
            (first (filter #(= floor (:AOID %)) floors-for-date))
            floors-for-date)))

(defn read-rooms
    [areal building floor]
    (if areal
        (if building
            (if floor
                (let [prefix (str areal "." building "." floor ".")]
                    (filter #(.startsWith (:AOID %) prefix) @rooms))
                (let [prefix (str areal "." building ".")]
                    (filter #(.startsWith (:AOID %) prefix) @rooms)))
            (let [prefix (str areal ".")]
                (filter #(.startsWith (:AOID %) prefix) @rooms)))
        @rooms))
