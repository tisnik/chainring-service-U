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

(ns chainring-service.rest-api-utils
    "Utility functions used by REST API handlers.

    Author: Pavel Tisnovsky")


(require '[ring.util.response         :as http-response])
(require '[clojure.data.json          :as json])
(require '[clj-fileutils.fileutils    :as file-utils])

(require '[chainring-service.config   :as config])


; HTTP codes used by several REST API responses
(def http-codes
    "HTTP codes used by several REST API responses."
    {
    :ok                    200
    :bad-request           400
    :not-found             404
    :internal-server-error 500
    :not-implemented       501})


(def timeformatter (new java.text.SimpleDateFormat "yyyy-MM-dd HH:mm:ss"))


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


(defn get-timestamp
    []
    (->> (new java.util.Date)
         (.format timeformatter)))

