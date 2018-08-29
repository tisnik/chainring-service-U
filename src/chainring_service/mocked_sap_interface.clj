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


(def areals-data-file
    "data/areals.csv")

(def buildings-data-file
    "data/buildings.csv")

(def floors-data-file
    "data/floors.csv")

(def rooms-data-file
    "data/rooms.csv")

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

(defn load-all-data-files
    []
    (reset! areals    (load-csv areals-data-file))
    (reset! buildings (load-csv buildings-data-file))
    (reset! floors    (load-csv floors-data-file))
    (reset! rooms     (load-csv rooms-data-file)))

(load-all-data-files)

(defn reload-mock-data
    []
    (log/info "Reload mock data begin")
    (load-all-data-files)
    (let [status {:areals    (count @areals)
                  :buildings (count @buildings)
                  :floors    (count @floors)
                  :rooms     (count @rooms)}]
        (log/info "Reload mock data end")
        status))

(defn read-areals
    []
    @areals)

(defn read-areal-info
    [areal]
    (first (filter #(= areal (:AOID %)) @areals)))

(defn read-buildings
    [areal]
    (if areal
        (let [prefix (str areal ".")]
            (filter #(.startsWith (:AOID %) prefix) @buildings))
        @buildings))

(defn read-floors
    [areal building]
    (if areal
        (if building
            (let [prefix (str areal "." building ".")]
                (filter #(.startsWith (:AOID %) prefix) @floors))
            (let [prefix (str areal ".")]
                (filter #(.startsWith (:AOID %) prefix) @floors)))
        @floors))

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
