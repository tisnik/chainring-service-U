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
    [response request http-code]
    (if (config/pretty-print? request)
        (-> (http-response/response (with-out-str (json/pprint response)))
            (http-response/content-type "application/json")
            (http-response/status (get http-codes http-code)))
        (-> (http-response/response (json/write-str response))
            (http-response/content-type "application/json")
            (http-response/status (get http-codes http-code)))))

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
    (let [response {"/"     "the schema"
                    "/info" "basic info about the service"}]
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
