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


(defn get-aoid-row-i
    [return-table i]
    (.setRow return-table i)
    {:ID         (.getString return-table "INTRENO")
     :AOID       (.getString return-table "AOID")
     :Label      (.getString return-table "XAO")
     :FunctionId (.getString return-table "AOFUNCTION")
     :Function   (.getString return-table "XMAOFUNCTION")
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
     :Atribut     (.getString return-table "TITLE")
     :radiobutton (.getString return-table "RAD")
     :dyncolors   (.getString return-table "DYN")})


(defn get-attribute-value-row-i
    [return-table i]
    (.setRow return-table i)
    {:ID          (.getString return-table "KEY")
     :value       (.getString return-table "VALUE")})


(defn get-sap-function
    [destination function-name]
    (try
        (-> destination
            .getRepository
            (.getFunction function-name))
        (catch Exception e
            (log/error "SAP error" e)
            nil)))


(defn get-aoids-from-sap-table
    [function table-name]
    (let [return-table (.getTable (.getTableParameterList function) table-name)]
          (for [i (range (.getNumRows return-table))]
               (get-aoid-row-i return-table i))))


(defn date->sap
    [date]
    (clojure.string/replace date #"-" ""))


(defn read-areals
    "Read list of areals from SAP."
    [valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_AREAS")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              {:date-from valid-from
               :areals (get-aoids-from-sap-table function "ET_AREAS")})))


(defn read-buildings
    "Read list of buildings from SAP."
    [areal-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_BUILDINGS")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
              (.setValue (.getImportParameterList function) "I_AREA", areal-id)
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              (get-aoids-from-sap-table function "ET_BUILDINGS"))))


(defn read-floors
    [areal building-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_FLOORS")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
              (.setValue (.getImportParameterList function) "I_BUILDING", building-id)
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              (get-aoids-from-sap-table function "ET_FLOORS"))))


(defn read-rooms
    [floor-id valid-from]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ROOMS")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
              (.setValue (.getImportParameterList function) "I_FLOOR", floor-id)
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              (get-aoids-from-sap-table function "ET_ROOMS"))))


(defn read-room-attribute-types
    []
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ATTRS")]
    
          (when function
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              (let [return-table (.getTable (.getTableParameterList function) "ET_ATTRS")]
                    (let [result (for [i (range (.getNumRows return-table))]
                                 (get-attribute-row-i return-table i))]
                                 (conj result {:ID "typ"
                                               :Atribut "Typ místnosti"
                                               :radiobutton nil
                                               :dyncolor nil
                           }))))))


(defn values-for-attribute
    [attribute]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_ATTR_VALUES")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_ID", attribute)
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
              (let [return-table (.getTable (.getTableParameterList function) "ET_VALUES")]
                    (for [i (range (.getNumRows return-table))]
                         (get-attribute-value-row-i return-table i))))))


(defn read-room-common-possible-attributes
    [floor-id valid-from attribute-id]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_FLOOR_VALUES")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
              (.setValue (.getImportParameterList function) "I_FLOOR", floor-id)
              (.setValue (.getImportParameterList function) "I_ID", attribute-id)
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
                  (let [return-table (.getTable (.getTableParameterList function) "ET_VALUES")]
                        (distinct (for [i (range (.getNumRows return-table))]
                             (:value (get-room-row-i return-table i))))))))


(defn read-common-rooms-attribute
    [floor-id valid-from attribute-id]
    (let [destination (JCoDestinationManager/getDestination "ABAP_AS_WITH_POOL")
          function    (get-sap-function destination "Z_CAD_GET_FLOOR_VALUES")]
    
          (when function
              (.setValue (.getImportParameterList function) "I_DATE", (date->sap valid-from))
              (.setValue (.getImportParameterList function) "I_FLOOR", floor-id)
              (.setValue (.getImportParameterList function) "I_ID", attribute-id)
        
              (try
                  (.execute function destination)
                  (catch AbapException e
                      (println e)
                      nil))
                  (let [return-table (.getTable (.getTableParameterList function) "ET_ROOMS")]
                        (for [i (range (.getNumRows return-table))]
                             (get-room-row-i return-table i))))))


(defn read-room-type
    [floor-id valid-from]
    (let [rooms (read-rooms floor-id valid-from)]
         (for [room rooms] {:AOID  (:AOID room)
                            :key   (:FunctionId room)
                            :value (:Function room)})))


(defn read-room-type-possible-attributes
    [floor-id valid-from]
    (let [rooms (read-rooms floor-id valid-from)]
         (distinct (for [room rooms] (:Function room)))))


(defn read-rooms-attribute
    [floor-id valid-from attribute-id]
    (if (and floor-id valid-from attribute-id)
        (if (= attribute-id "typ")
            (read-room-type floor-id valid-from)
            (read-common-rooms-attribute floor-id valid-from attribute-id))))


(defn read-rooms-possible-attributes
    [floor-id valid-from attribute-id]
    (if (and floor-id valid-from attribute-id)
        (if (= attribute-id "typ")
            (read-room-type-possible-attributes floor-id valid-from)
            (read-room-common-possible-attributes floor-id valid-from attribute-id))))


(defn read-areal-info
    "Read info about selected areal."
    [areal valid-from]
    (let [areals (read-areals valid-from)]
         (if areals
             (first (filter #(= areal (:AOID %)) (:areals areals)))
             nil)))


(defn read-building-info
    "Read info about selected building."
    [areal building valid-from]
    (let [buildings (read-buildings areal valid-from)]
        (if buildings
            (first (filter #(= building (:AOID %)) buildings))
            nil)))


(defn read-floor-info
    "Read info about floor."
    [areal building floor valid-from]
    (let [floors (read-floors areal building valid-from)]
        (if floors
            (first (filter #(= floor (:AOID %)) floors))
            nil)))
