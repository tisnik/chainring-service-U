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

(ns chainring-service.rest-api
    "Handler for all REST API calls.

    Author: Pavel Tisnovsky")


(require '[clojure.pprint                          :as pprint])
(require '[clojure.tools.logging                   :as log])

(require '[chainring-service.db-interface          :as db-interface])
(require '[chainring-service.config                :as config])
(require '[chainring-service.drawings-storage      :as drawings-storage])
(require '[chainring-service.drawings-cache        :as drawings-cache])

(require '[chainring-service.sap-interface         :as sap-interface])
(require '[chainring-service.mocked-sap-interface  :as mocked-sap-interface])
(require '[chainring-service.rest-api-utils        :as rest-api-utils])

(use     '[clj-utils.utils])


(defn unknown-endpoint-handler
    "Process any unknown endpoints."
    [request uri]
    (rest-api-utils/send-error-response "unknown endpoint" uri request :bad-request))


(defn toplevel-handler
    "REST API handler for the /api endpoint."
    [request api-full-prefix]
    (let [response {api-full-prefix "current REST API version endpoint"}]
        (rest-api-utils/send-response response request)))


(defn api-info-handler
    "REST API handler for the /api/{version} endpoint."
    [request api-prefix]
    (let [response {(str api-prefix "/")               "the schema"
                    (str api-prefix "/info")           "basic info about the service"
                    (str api-prefix "/liveness")       "check the liveness of the service"
                    (str api-prefix "/readiness")      "check the readiness of the service and all subcomponents"
                    (str api-prefix "/config")         "actual configuration"
                    (str api-prefix "/areals")         "list of areals"
                    (str api-prefix "/buildings")      "list of buildings"
                    (str api-prefix "/project")        "project metadata"
                    (str api-prefix "/building")       "building metadata"
                    (str api-prefix "/drawing")        "drawing metadata"
                    (str api-prefix "/drawings-cache") "drawings cache statistic"}]
        (rest-api-utils/send-response response request)))


(defn info-handler
    "REST API handler for the /api/{version}/info endpoint."
    [request]
    (let [response {:name            "Chainring Service"
                    :service-version (config/get-version request)
                    :db-version      (config/get-db-version request)
                    :api-prefix      (config/get-api-prefix request)
                    :api-version     (config/get-api-version request)
                    :full-prefix     (config/get-api-full-prefix request)}]
                    ;:hostname   hostname :test "/api"}]
        (rest-api-utils/send-response response request)))


(defn liveness-handler
    "REST API handler for the /api/{version}/liveness endpoint."
    [request]
    (let [response {:status "ok"}]
        (rest-api-utils/send-response response request)))


(defn readiness-handler
    "REST API handler for the /api/{version}/readiness endpoint."
    [request]
    (let [response {:status "ok"}]
        (rest-api-utils/send-response response request)))


(defn config-handler
    "REST API handler for the /api/{version}/config endpoint."
    [request]
    (let [response {:configuration (:configuration request)}]
        (rest-api-utils/send-response response request)))


(defn list-all-aoids
    "REST API handler for the /api/{version}/aoids endpoint."
    [request uri]
    (try
        (let [start-time (System/currentTimeMillis)
              areals     (sap-interface/call-sap-interface request "read-areals" nil)
              buildings  (sap-interface/call-sap-interface request "read-buildings" nil nil)
              floors     (sap-interface/call-sap-interface request "read-floors" nil nil nil)
              end-time   (System/currentTimeMillis)
              timestamp  (rest-api-utils/get-timestamp)
              response {:status    :ok
                        :duration  (- end-time start-time)
                        :timestamp timestamp
                        :areals    areals
                        :buildings buildings
                        :floors    floors}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "read-all-aoids")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn list-of-areals-handler
    "REST API handler for the /api/{version}/areals endpoint."
    [request uri]
    (try
        (let [start-time (System/currentTimeMillis)
              params     (:params request)
              date-from  (get params "valid-from") 
              result     (sap-interface/call-sap-interface request "read-areals" date-from)
              end-time   (System/currentTimeMillis)
              timestamp  (rest-api-utils/get-timestamp)
              response {:status    :ok
                        :duration  (- end-time start-time)
                        :timestamp timestamp
                        :date-from (:date-from result)
                        :areals    (:areals result)}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "read-areals")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn list-of-buildings-handler
    "REST API handler for the /api/{version}/buildings endpoint."
    [request uri]
    (try
        (let [start-time (System/currentTimeMillis)
              params     (:params request)
              areal-id   (get params "areal-id")
              date-from  (get params "valid-from") 
              buildings  (sap-interface/call-sap-interface request "read-buildings" areal-id date-from)
              end-time   (System/currentTimeMillis)
              timestamp  (rest-api-utils/get-timestamp)
              response {:status    :ok
                        :duration  (- end-time start-time)
                        :timestamp timestamp
                        :areal-id  areal-id
                        :buildings buildings}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "read-buildings")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn list-of-floors-handler
    "REST API handler for the /api/{version}/floors endpoint."
    [request uri]
    (try
        (let [start-time  (System/currentTimeMillis)
              params      (:params request)
              areal-id    (get params "areal-id")
              building-id (get params "building-id")
              date-from   (get params "valid-from") 
              floors      (sap-interface/call-sap-interface request "read-floors" areal-id building-id date-from)
              end-time    (System/currentTimeMillis)
              timestamp   (rest-api-utils/get-timestamp)
              response {:status       :ok
                        :duration     (- end-time start-time)
                        :timestamp    timestamp
                        :areal-id     areal-id
                        :building-id  building-id
                        :floors       floors}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "read-floors")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn list-all-dates-from
    [request uri]
    (try
        (let [start-time (System/currentTimeMillis)
              dates      (sap-interface/call-sap-interface request "read-all-dates-from")
              end-time   (System/currentTimeMillis)
              timestamp  (rest-api-utils/get-timestamp)
              response {:status     :ok
                        :duration   (- end-time start-time)
                        :timestamp  timestamp
                        :dates-from dates}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "read-dates-from")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn nearest-date-from
    [request uri]
    (try
        (let [start-time      (System/currentTimeMillis)
              params          (:params request)
              date-from       (get params "date-from")
              real-date-from  (sap-interface/call-sap-interface request "get-real-date-from" date-from)
              end-time        (System/currentTimeMillis)
              timestamp       (rest-api-utils/get-timestamp)
              response {:status     :ok
                        :duration   (- end-time start-time)
                        :timestamp  timestamp
                        :date-from  real-date-from}]
            (rest-api-utils/send-response response request))
        (catch Exception e
            (log/error e "nearest-date-from")
            (rest-api-utils/send-error-response "SAP Access error" (str e) request :internal-server-error))))


(defn read-project-info
    "REST API handler for /api/{version}/project endpoint."
    [project-id]
    (let [project-info (db-interface/read-project-info project-id)
          buildings    (db-interface/read-building-list project-id)]
         (log/info "Project ID:" project-id)
         (log/info "Project info:" project-info)
         (log/info "Buildings:" buildings)
         ; result structure
         {:id        project-id
          :info      project-info
          :buildings buildings}))


(defn read-building-info
    "REST API handler for /api/{version}/building-info endpoint."
    [building-id]
    (let [building-info (db-interface/read-building-info building-id)
          floors        (db-interface/read-floor-list building-id)]
          (log/info "Building ID:" building-id)
          (log/info "Building info:" building-info)
          (log/info "Floors:" floors)
          ; result response
          {:id      building-id
           :info    building-info
           :floors  floors}))


(defn project-handler
    "REST API handler for the /api/{version}/project request."
    [request uri]
    (let [params       (:params request)
          project-id   (get params "project-id")]
          (if project-id
              (rest-api-utils/send-response (read-project-info project-id) request)
              (rest-api-utils/send-error-response "you need to specify project ID" uri request :internal-server-error))))


(defn building-handler
    "REST API handler for the /api/{version}/building endpoint."
    [request uri]
    (let [params        (:params request)
          building-id   (get params "building-id")]
          (if building-id
              (rest-api-utils/send-response (read-building-info building-id) request)
              (rest-api-utils/send-error-response "you need to specify building ID" uri request :internal-server-error))))


(defn floor-handler
    "REST API handler for the /api/{version}/floor endpoint."
    [request uri]
    (let [params     (:params request)
          floor-id   (get params "floor-id")
          floor-info (db-interface/read-floor-info floor-id)]
          (log/info "Floor ID:" floor-id)
          (log/info "Floor info" floor-info)
          (if floor-id
              (let [drawings (db-interface/read-drawing-list floor-id)]
                  (log/info "Drawings" drawings)
                  (rest-api-utils/send-response drawings request))
              (rest-api-utils/send-error-response "you need to specify floor ID" uri request :internal-server-error))))


(defn drawing-handler
    "REST API handler for the /api/{version}/drawing endpoint."
    [request uri]
    (let [params        (:params request)
          drawing-id    (get params "drawing-id")
          drawing-info  (db-interface/read-drawing-info drawing-id)
          rooms         (db-interface/read-room-list drawing-id)
          result        {:drawing-info drawing-info
                         :rooms rooms}]
          (log/info "Drawing ID:" drawing-id)
          (log/info "Drawing info" drawing-info)
          (log/info "Rooms" rooms)
          (if drawing-id
              (if drawing-info
                  (rest-api-utils/send-response result request)
                  (rest-api-utils/send-error-response "no drawing info" uri request :internal-server-error))
              (rest-api-utils/send-error-response "you need to specify drawing ID" uri request :internal-server-error))))


(defn all-buildings
    "REST API handler for the /api/{version}/buildings endpoint."
    [request uri]
    (let [params    (:params request)
          buildings (db-interface/read-all-buildings)]
         (rest-api-utils/send-response buildings request)))


(defn all-floors
    "REST API handler for the /api/{version}/floors endpoint."
    [request uri]
    (let [params    (:params request)
          floors    (db-interface/read-all-floors)]
         (rest-api-utils/send-response floors request)))


(defn sap-reload-mock-data
    [request uri]
    (let [params            (:params request)
          mock-sap-response (config/mock-sap-response? request)]
          (if mock-sap-response
              (let [status (mocked-sap-interface/reload-mock-data)]
                    (rest-api-utils/send-response status request)))))


(defn get-sap-href
    "Get the HREF to SAP prefix from the configuration."
    [configuration object-type]
    (condp = object-type
        "room" (str "SAPEVENT:ROOM_CLICK?" (:param-name-to-sap-room-id configuration) "=")
        ""))


(defn get-sap-selector
    "Get the SAP selector from the URI."
    [uri]
    (if (.contains uri "/")
        (subs uri (inc (.lastIndexOf uri "/")))))


(defn sap-href-handler
    "REST API handler for the /api/{version}/sap-href endpoint."
    [request uri]
    (let [configuration (-> request :configuration :sap-interface)
          object-type   (get-sap-selector uri)
          href          (get-sap-href configuration object-type)]
          (rest-api-utils/send-response href request)))


(defn sap-floors
    [request uri]
    (let [params            (:params request)
          areal             (get params "areal")
          building          (get params "building")
          sap-response      (sap-interface/call-sap-interface request "read-floors" areal building)]
        (rest-api-utils/send-response sap-response request)))


(defn sap-rooms
    [request uri]
    (let [params (:params request)
          areal             (get params "areal")
          building          (get params "building")
          floor             (get params "floor")
          sap-response      (sap-interface/call-sap-interface request "read-rooms" areal building floor)]
        (rest-api-utils/send-response sap-response request)))


(defn sap-debug-handler
    "REST API handler for the /api/{version}/sap-debug endpoint."
    [request uri]
    (let [params    (:params request)]
         (log/info "fake SAP handler")
         (log/info params)
         (rest-api-utils/send-response params request)))

(defn read-attributes-for-rooms
    [attribute]
    [1 2 3 4])

(defn rooms-attribute
    [request uri]
    (let [params     (:params request)
          floor      (get params "floor-aoid")
          valid-from (get params "valid_from")
          attribute  (get params "attribute")
          sap-response (sap-interface/call-sap-interface request "read-rooms-attribute" floor valid-from attribute)]
        (rest-api-utils/send-response sap-response request)
    ))

(defn rooms-with-attribute
    [request uri]
    (let [params     (:params request)
          attribute  (get params "attribute")
          project    (get params "project-aoid")
          building   (get params "building-aoid")
          floor      (get params "floor-aoid")
          valid_from (get params "valid_from")]
          (let [rooms     (read-attributes-for-rooms attribute)]
              (rest-api-utils/send-response rooms request)
          )))

(defn all-drawings-handler
    "REST API handler for the /api/{version}/drawings endpoint."
    [request uri]
    (let [params    (:params request)
          buildings (db-interface/read-all-buildings)
          floors    (db-interface/read-all-floors)
          drawings  (db-interface/read-all-drawings)
          response  {
                     :buildings buildings
                     :floors    floors
                     :drawings  drawings}
         ]
         (rest-api-utils/send-response response request)))


(defn drawings-cache-info-handler
    "REST API handler for the /api/{version}/drawings-cache endpoint."
    [request]
    (let [response {:cache-utilization @drawings-cache/hit-counters
                    :cache-size (drawings-cache/cache-size)}]
         (rest-api-utils/send-response response request)))

(defn store-drawing-raw-data
    "REST API handler for the /api/{version}/drawing-raw-data endpoint."
    [request]
    (let [params     (:params request)
          drawing-id (get params "drawing")
          raw-data   (rest-api-utils/read-request-body request)]
        (if (and drawing-id raw-data)
            (try
                (db-interface/store-drawing-raw-data drawing-id raw-data)
                (rest-api-utils/send-ok-response "Drawing has been written into the database" request)
                (catch Exception e
                    (log/error e)
                    (rest-api-utils/send-error-response "exception occured during write" (.getMessage e) request :internal-server-error)))
            (rest-api-utils/send-error-response "send drawing ID as parameter and raw data in the body" "wrong input" request :bad-request)
        )))


(defn missing-parameter
    "Send error response when some required parameter is missing."
    [request parameter]
    (let [message (str "missing required parameter '" parameter "'")]
        (rest-api-utils/send-error-response message "wrong input" request :bad-request)))


(defn try-to-load-drawing
    "Try to load the drawing from the filesystem."
    [drawing-id store-format configuration]
    (let [id        (parse-int drawing-id)
          directory (-> configuration :drawings :directory)]
          {"directory" directory}))


(defn deserialize-drawing
    "Deserialize the drawing from the filesystem."
    [request]
    (let [params        (:params request)
          configuration (:configuration request)
          drawing-id    (get params "drawing")
          store-format  (get params "format")]
          (cond (and drawing-id format)
                (try
                    (-> (try-to-load-drawing drawing-id store-format configuration)
                        (rest-api-utils/send-response request 200))
                    (catch Exception e
                        (log/error e)
                        (rest-api-utils/send-error-response "exception occured during write" (.getMessage e) request :internal-server-error)))
                (nil? drawing-id)
                (missing-parameter request "drawing")
                (nil? store-format)
                (missing-parameter request "format")
    )))


(defn try-to-store-drawing
    "Try to store the drawing onto the filesystem."
    [drawing-id store-format raw-data configuration]
    (let [directory (-> configuration :drawings :directory)]
          (drawings-storage/store-drawing-as drawing-id directory store-format raw-data)))


(defn serialize-drawing
    "Serialize the drawing onto the filesystem. Drawing data is sent in JSON format in the body of the POST request."
    [request]
    (let [params        (:params request)
          configuration (:configuration request)
          drawing-id    (get params "drawing")
          store-format  (get params "format")
          raw-data      (rest-api-utils/read-request-body request)]
          (cond (and drawing-id format raw-data)
                (try
                    (try-to-store-drawing drawing-id store-format raw-data configuration)
                    (rest-api-utils/send-ok-response "Drawing has been saved" request)
                    (catch Exception e
                        (log/error e)
                        (rest-api-utils/send-error-response "exception occured during write" (.getMessage e) request :internal-server-error)))
                (nil? drawing-id)
                (missing-parameter request "drawing")
                (nil? store-format)
                (missing-parameter request "format")
                (nil? raw-data)
                (rest-api-utils/send-error-response "missing body with drawing data" "wrong input" request :bad-request))))
