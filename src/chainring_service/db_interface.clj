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
    (try
        (jdbc/query db-spec/zg-db
                        ["select id, name from project order by name"])
        (catch Exception e
            (log/error e "read-project-list")
            [])))

(defn read-buildings
    [project-id]
    (try
        (jdbc/query db-spec/zg-db
                        ["select id, name from building where project=? order by name" project-id])
        (catch Exception e
            (log/error e "read-buildings")
            [])))

(defn read-drawings
    []
    (try
        (jdbc/query db-spec/zg-db
                        ["select id, name from drawings order by name"])
        (catch Exception e
            (log/error e "read drawings")
            [])))

