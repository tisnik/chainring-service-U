(ns chainring-service.db-interface
    "Namespace that contains interface to the database.")

(require '[clojure.java.jdbc     :as jdbc])
(require '[clojure.tools.logging :as log])

(require '[chainring-service.db-spec :as db-spec])

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

