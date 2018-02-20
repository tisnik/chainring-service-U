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

(ns chainring-service.db-interface
    "Namespace that contains interface to the database.")

(require '[clojure.java.jdbc     :as jdbc])
(require '[clojure.tools.logging :as log])

(require '[chainring-service.db-spec :as db-spec])

(defn simple-query-sequence
    "Perform a simple query from the database. Sequence of results are returned."
    [query operation]
    (try
        (jdbc/query db-spec/chainring-db query)
        (catch Exception e
            (log/error e operation)
            [])))

(defn simple-query
    "Perform a simple query from the database. Only the first result is returned."
    [query operation]
    (try
        (-> (jdbc/query db-spec/chainring-db query)
            first)
        (catch Exception e
            (log/error e operation)
            [])))

(defn simple-query-selector
    "Perform a simple query from the database. Only one value from the first result is returned."
    [query selector operation]
    (try
        (-> (jdbc/query db-spec/chainring-db query)
            first
            (get selector))
        (catch Exception e
            (log/error e operation)
            [])))

(defn read-project-list
    "Read list of all projects."
    []
    (simple-query-sequence ["select id, aoid, name, created from project order by name"]
                  "read-project-list"))

(defn read-project-name
    "Read project name for given project ID."
    [project-id]
    (if project-id
        (simple-query-selector ["select name from project where id=?" project-id] :name "read-project-name")))

(defn read-project-info
    "Read project info for given project ID."
    [project-id]
    (if project-id
        (simple-query ["select id, aoid, name, created from project where id=?" project-id] "read-project-info")))

(defn read-detailed-project-info
    "Read detailed project info for given project ID."
    [project-id]
    (if project-id
        (simple-query ["select * from project where id=?" project-id] "read-detailed-project-info")))

(defn read-all-buildings
    "Read list of all buildings."
    []
    (simple-query-sequence ["select id, aoid, project, name, created from building order by name"]
                  "read-all-buildings"))

(defn read-building-list
    "Read list of buildings for given project ID."
    [project-id]
    (if project-id
        (simple-query-sequence ["select id, aoid, name, created from building where project=? order by name" project-id]
                      "read-building-list")))

(defn read-building-count-for-project
    [project-id]
    (if project-id
        (simple-query ["select count(*) as cnt from building where project=?" project-id] "read-building-count")))

(defn read-building-info
    [building-id]
    (if building-id
        (simple-query ["select * from building where id=?" building-id] "read-building-info")))

(defn read-floor-count-for-building
    [building-id]
    (if building-id
        (simple-query ["select count(*) as cnt from floor where building=?" building-id] "read-floor-count-for-building")))

(defn read-drawing-count-for-floor
    [floor-id]
    (if floor-id
        (simple-query ["select count(*) as cnt from drawing where floor=?" floor-id] "read-drawing-count-for-floor")))

(defn read-all-floors
    "Read list of all floors."
    []
    (simple-query-sequence ["select id, aoid, building, name, created from floor f order by id"]
                  "read-all-floors"))

(defn read-floor-list
    [building-id]
    (if building-id
        (simple-query-sequence ["select id, aoid, name, created, (select count(*) as cnt from drawing where floor=f.id) as drawings from floor f where building=? order by id" building-id]
                      "read-floor-list")))

(defn read-floor-info
    [floor-id]
    (if floor-id
        (simple-query ["select * from floor where id=?" floor-id] "read-floor-info")))

(defn read-all-drawings
    []
    (simple-query-sequence ["select id, aoid, floor, name, created, modified, version from drawing order by name"]
                  "read-all-drawings"))

(defn read-drawing-list
    [floor-id]
    (if floor-id
        (simple-query-sequence ["select id, aoid, name, created, modified, version from drawing where floor=? order by aoid" floor-id]
                      "read-drawing-list")))

(defn read-drawing-info
    [drawing-id]
    (if drawing-id
        (simple-query ["select * from drawing where id=?" drawing-id] "read-drawing-info")))

(defn read-room-list
    [drawing-id]
    (if drawing-id
        (simple-query-sequence ["select * from room where drawing=?" drawing-id] "read-room-list")))
 
(defn read-sap-room-list
    [floor-id version]
    (if (and floor-id version)
        (simple-query-sequence ["select *, (select label from room_type where room_type.id=s.room_type) as room_type_str from sap_room s where floor=? and version=? order by aoid" floor-id version] "read-sap-room-list")))

(defn read-sap-room-count
    [floor-id version]
    (if floor-id
        (simple-query ["select count(*) as cnt from sap_room where floor=? and version=?" floor-id version] "read-sap-room-count")))

(defn store-drawing-raw-data
    [drawing-id raw-data]
    (if (and drawing-id raw-data)
        (jdbc/insert! db-spec/chainring-db
            :drawing_raw_data {:drawing drawing-id
                               :raw_data raw-data})))

(defn get-new-user-id
    []
    (get (simple-query ["select seq+1 as id from sqlite_sequence where name='USERS'"] "get-new-user-id") :id))

(defn get-record-count
    [table]
    (get (simple-query [(str "select count(*) as cnt from " table)] "get-record-count") :cnt))

(defn get-db-status
    []
    {:projects      (get-record-count "project")
     :buildings     (get-record-count "building")
     :floors        (get-record-count "floor")
     :sap-rooms     (get-record-count "sap_room")
     :drawings      (get-record-count "drawing")
     :drawings-data (get-record-count "drawing_raw_data")
     :drawing-rooms (get-record-count "drawing_room")
     :users         (get-record-count "users")
    })

(defn update-or-insert!
    "Updates columns or inserts a new row in the specified table"
    [database table row where-clause]
    (jdbc/with-db-transaction [t-con database]
        (let [result (jdbc/update! t-con table row where-clause)]
            (if (zero? (first result))
                (jdbc/insert! t-con table row)
                result))))

(defn store-user-settings
    [user-id resolution selected-room-color pen-width]
    (if (and user-id resolution selected-room-color pen-width)
        (update-or-insert! db-spec/chainring-db :users
            {:resolution resolution
             :selected_room_color selected-room-color
             :pen_width pen-width}
            ["id = ?" user-id])))

