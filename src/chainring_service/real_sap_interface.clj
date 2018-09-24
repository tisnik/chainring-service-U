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

(import com.sap.conn.jco.AbapException)
(import com.sap.conn.jco.JCoContext)
(import com.sap.conn.jco.JCoDestination)
(import com.sap.conn.jco.JCoDestinationManager)
(import com.sap.conn.jco.JCoException)
(import com.sap.conn.jco.JCoField)
(import com.sap.conn.jco.JCoFunction)
(import com.sap.conn.jco.JCoFunctionTemplate)
(import com.sap.conn.jco.JCoStructure)
(import com.sap.conn.jco.JCoTable)
(import com.sap.conn.jco.ext.DestinationDataProvider)

(require '[clojure.tools.logging        :as log])
(require '[chainring-service.csv-loader :as csv-loader])


(defn get-aoid-row-i
    [return-table i]
    (.setRow return-table i)
    {:AOID       (.getString return-table "AOID")
     :Label      (.getString return-table "XAO")
     :valid-from (.getString return-table "VALIDFROM")
     :valid-to   (.getString return-table "VALIDTO")})


(defn get-room-row-i
    [return-table i]
    (.setRow return-table i)
    {:AOID       (.getString return-table "AOID")
     :Label      (.getString return-table "XAO")
     :key        (.getString return-table "KEY")
     :value      (.getString return-table "VALUE")})


(defn get-attribute-row-i
    [return-table i]
    (.setRow return-table i)
    {:ID          (.getString return-table "ID")
     :Label       (.getString return-table "TITLE")
     :radiobutton (.getString return-table "RAD")
     :dyncolors   (.getString return-table "DYN")})


(defn get-attribute-value-row-i
    [return-table i]
    (.setRow return-table i)
    {:ID          (.getString return-table "KEY")
     :value       (.getString return-table "VALUE")})


(defn get-sap-function
    [destination function-name]
    (-> destination
        .getRepository
        (.getFunction function-name)))


(defn get-aoids-from-sap-table
    [function table-name]
    (let [return-table (.getTable (.getTableParameterList function) table-name)]
          (for [i (range (.getNumRows return-table))]
               (get-aoid-row-i return-table i))))


(defn date->sap
    [date]
    (clojure.string/replace date #"-" ""))


(defn read-areals
    [valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_AREAS")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
          {:date-from valid-from
           :areals (get-aoids-from-sap-table function "ET_AREAS")}))


(defn read-buildings
    [areal-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_BUILDINGS")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
          (.setValue (.getImportParameterList function) "I_AREA", areal-id)
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
          (get-aoids-from-sap-table function "ET_BUILDINGS")))


(defn floor-list
    [building-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_FLOORS")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_DATE", "20180101")
          (.setValue (.getImportParameterList function) "I_BUILDING", building-id)
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
          (get-aoids-from-sap-table function "ET_FLOORS")))


(defn room-list
    [floor-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ROOMS")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_DATE", "20180101")
          (.setValue (.getImportParameterList function) "I_FLOOR", floor-id)
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
          (get-aoids-from-sap-table function "ET_ROOMS")))


(defn all-attributes
    []
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ATTRS")]
    
          (if (not function) nil)
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
    (let [return-table (.getTable (.getTableParameterList function) "ET_ATTRS")]
          (for [i (range (.getNumRows return-table))]
               (get-attribute-row-i return-table i)))))


(defn values-for-attribute
    [attribute]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ATTR_VALUES")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_ID", attribute)
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
    (let [return-table (.getTable (.getTableParameterList function) "ET_VALUES")]
          (for [i (range (.getNumRows return-table))]
               (get-attribute-value-row-i return-table i)))))


(defn room-attributes
    [floor-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_FLOOR_VALUES")]
    
          (if (not function) nil)
    
          (.setValue (.getImportParameterList function) "I_DATE", "20180101")
          (.setValue (.getImportParameterList function) "I_FLOOR", floor-id)
          (.setValue (.getImportParameterList function) "I_ID", "OB")
    
          (try
              (.execute function destination)
              (catch AbapException e
                  (println e)
                  nil))
    (let [return-table (.getTable (.getTableParameterList function) "ET_ROOMS")]
          (for [i (range (.getNumRows return-table))]
               (get-room-row-i return-table i)))))


;(defn -main
;    [& args]
;    (println (areal-list "20000101"))
;    (println)
;    (println (building-list "BARR" "20000101"))
;    (println (building-list "HOST" "20000101"))
;    (println)
;    (println (floor-list "HOST.10" "20000101"))
;    (println (floor-list "HOST.15" "20000101"))
;    (println (floor-list "BARR/10" "20000101"))
;    (println)
;    (println (room-list "HOST.10.0P" "20000101"))
;    (println (room-list "HOST.10.1P" "20000101"))
;    (println (room-list "BARR/10/1S" "20000101"))
;    (println)
;    (println (all-attributes))
;    (println)
;    (println)
;    (println (values-for-attribute "OB"))
;    (println)
;    (println)
;    (println)
;    (println (room-attributes "HOST.10.0P" "20000101"))
;)
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


(defn read-areal-info
    [areal valid-from]
    (let [areals (read-areals valid-from)]
         (if areals
             (first (filter #(= areal (:AOID %)) (:areals areals)))
             nil)))


(defn read-floors
    [areal building]
    [1 2 3 4])

(defn read-rooms
    [areal building floor]
    [1 2 3 4 5])

(defn read-building-info
    [building-id]
    [1 2])

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


(defn today?
    [valid-from]
    (if valid-from
        (let [timeformatter (new java.text.SimpleDateFormat "yyyy-MM-dd")
              now           (new java.util.Date)
              now-str       (.format timeformatter now)]
              (= now-str valid-from))
        true))


(defn read-buildings-----
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
