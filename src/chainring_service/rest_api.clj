;
;  (C) Copyright 2017  Pavel Tisnovsky
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
    "Handler for all REST API calls.")

(require '[ring.util.response         :as http-response])
(require '[clojure.pprint             :as pprint])
(require '[clojure.data.json          :as json])
(require '[clojure.tools.logging      :as log])
(require '[clj-fileutils.fileutils    :as file-utils])

(require '[chainring-service.db-interface  :as db-interface])
(require '[chainring-service.config   :as config])

; HTTP codes used by several REST API responses
(def http-codes {
    :ok                    200
    :bad-request           400
    :not-found             404
    :internal-server-error 500
    :not-implemented       501})

(defn read-request-body
    "Read all informations from the request body."
    [request]
    (file-utils/slurp- (:body request)))

(defn body->results
    "Try to parse the request body as JSON format."
    [body]
    (json/read-str body))

(defn send-response
    "Send normal response (with application/json MIME type) back to the client."
    ([response request http-code]
     (if (config/pretty-print? request)
         (-> (http-response/response (with-out-str (json/pprint response)))
             (http-response/content-type "application/json")
             (http-response/status (get http-codes http-code)))
         (-> (http-response/response (json/write-str response))
             (http-response/content-type "application/json")
             (http-response/status (get http-codes http-code)))))
    ([response request]
     (send-response response request :ok)))

(defn send-ok-response
    "Send ok response (with application/json MIME type) back to the client."
    [message request]
    (let [response {:status "ok"
                    :message message}]
        (send-response response request :ok)))

(defn send-error-response
    "Send error response (with application/json MIME type) back to the client."
    [message cause request http-code]
    (let [response {:status "error"
                    :message message
                    :cause cause}]
        (send-response response request http-code)))

(defn send-plain-response
    "Send a response (with application/json MIME type) back to the client."
    [response]
    (-> (http-response/response response)
        (http-response/content-type "application/json")))

(defn unknown-endpoint
    [request uri]
    (send-error-response "unknown endpoint" uri request :bad-request))

(defn api-info-handler
    "REST API handler for the /api request."
    [request]
    (let [response {"/"             "the schema"
                    "/info"         "basic info about the service"
                    "/project-list" "list of projects"
                    "/project"      "project metadata"
                    "/building"     "building metadata"
                    "/drawing"      "drawing metadata"}]
        (send-response response request)))

(defn info-handler
    "REST API handler for the /api/info request."
    [request]
    (let [response {:name       "Chainring Service"}]
                    ;:version    (config/get-version request)
                    ;:api_prefix (config/get-api-prefix request)
                    ;:gui_prefix (config/get-gui-prefix request)
                    ;:hostname   hostname :test "/api"}]
        (send-response response request)))

(defn project-list-handler
    "REST API handler for the /api/project-list request."
    [request uri]
    (let [projects      (db-interface/read-project-list)]
        (log/info "Projects:" projects)
        (if projects
            (send-response projects request)
            (send-error-response "database access error" uri request :internal-server-error))))

(defn read-project-info
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

(defn project-handler
    "REST API handler for the /api/project request."
    [request uri]
    (let [params       (:params request)
          project-id   (get params "project-id")]
          (if project-id
              (send-response (read-project-info project-id) request)
              (send-error-response "you need to specify project ID" uri request :internal-server-error))))

(defn building-handler
    "REST API handler for the /api/building request."
    [request uri]
    (let [params        (:params request)
          building-id   (get params "building-id")
          building-info (db-interface/read-building-info building-id)]
          (log/info "Building ID:" building-id)
          (log/info "Building info" building-info)
          (if building-id
              (let [floors (db-interface/read-floor-list building-id)]
                  (log/info "Floors" floors)
                  (send-response floors request))
              (send-error-response "you need to specify building ID" uri request :internal-server-error))))

(defn floor-handler
    "REST API handler for the /api/floor request."
    [request uri]
    (let [params     (:params request)
          floor-id   (get params "floor-id")
          floor-info (db-interface/read-floor-info floor-id)]
          (log/info "Floor ID:" floor-id)
          (log/info "Floor info" floor-info)
          (if floor-id
              (let [drawings (db-interface/read-drawing-list floor-id)]
                  (log/info "Drawings" drawings)
                  (send-response drawings request))
              (send-error-response "you need to specify floor ID" uri request :internal-server-error))))

(defn drawing-handler
    "REST API handler for the /api/drawing request."
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
                  (send-response result request)
                  (send-error-response "no drawing info" uri request :internal-server-error))
              (send-error-response "you need to specify drawing ID" uri request :internal-server-error))))

(defn store-drawing-raw-data
    [request]
    (let [params (:params request)
          drawing-id (get params "drawing")
          raw-data (read-request-body request)]
        (if (and drawing-id raw-data)
            (try
                (db-interface/store-drawing-raw-data drawing-id raw-data)
                (send-ok-response "Drawing has been written into the database" request)
                (catch Exception e
                    (log/error e)
                    (send-error-response "exception occured during write" (.getMessage e) request :internal-server-error)))
            (send-error-response "send drawing ID as parameter and raw data in the body" "wrong input" request :bad-request)
        )))
