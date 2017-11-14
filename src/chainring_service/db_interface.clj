(ns chainring-service.db-interface
    "Namespace that contains interface to the database.")

(require '[clojure.java.jdbc     :as jdbc])
(require '[clojure.tools.logging :as log])

(require '[chainring-service.db-spec :as db-spec])

(defn simple-query-sequence
    [query operation]
    (try
        (jdbc/query db-spec/zg-db query)
        (catch Exception e
            (log/error e operation)
            [])))

(defn simple-query
    [query operation]
    (try
        (-> (jdbc/query db-spec/zg-db query)
            first)
        (catch Exception e
            (log/error e operation)
            [])))

(defn simple-query-selector
    [query selector operation]
    (try
        (-> (jdbc/query db-spec/zg-db query)
            first
            (get selector))
        (catch Exception e
            (log/error e operation)
            [])))

(defn read-project-list
    []
    (simple-query-sequence ["select id, sap, name from project order by name"]
                  "read-project-list"))

(defn read-project-name
    [project-id]
    (simple-query-selector ["select name from project where id=?" project-id] :name "read-project-name"))

(defn read-project-info
    [project-id]
    (simple-query ["select * from project where id=?" project-id] "read-project-info"))

(defn read-building-list
    [project-id]
    (simple-query-sequence ["select id, sap, name from building where project=? order by name" project-id]
                  "read-building-list"))

(defn read-building-info
    [building-id]
    (simple-query ["select * from building where id=?" building-id] "read-building-info"))

(defn read-drawing-list
    [building-id]
    (simple-query-sequence ["select id, sap, name from drawing where building=? order by name" building-id]
                  "read-building-list"))

(defn read-drawing-info
    [drawing-id]
    (simple-query ["select * from drawing where id=?" drawing-id] "read-drawing-info"))

(defn read-room-list
    [drawing-id]
    (simple-query ["select * from rooms where drawing=?" drawing-id] "read-room-list"))