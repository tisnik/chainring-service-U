(ns chainring-service.db-spec
    "Namespace that contains configuration of all JDBC sources.")

(def zg-db
    "Specification for SQLite database used for storing all dictionaries."
    {:classname   "org.sqlite.JDBC"
     :subprotocol "sqlite"
     :subname     "chainring.db"
    })

