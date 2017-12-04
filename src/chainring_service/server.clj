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

(ns chainring-service.server
    "Server module with functions to accept requests and send response back to users via HTTP.")

(require '[ring.util.response      :as http-response])
(require '[clojure.tools.logging   :as log])

(require '[chainring-service.db-interface  :as db-interface])
(require '[chainring-service.html-renderer :as html-renderer])
(require '[chainring-service.rest-api      :as rest-api])
(require '[chainring-service.config        :as config])

(use     '[chainring-service.utils])


(defn return-file
    "Creates HTTP response containing content of specified file.
     Special value nil / HTTP response 404 is returned in case of any I/O error."
    [^String file-name content-type]
    (let [file (new java.io.File "www" file-name)]
        (log/info "Returning file " (.getAbsolutePath file))
        (if (.exists file)
            (-> (http-response/response file)
                (http-response/content-type content-type))
            (log/error "return-file(): can not access file: " (.getName file)))))

(defn finish-processing
    [request response-html]
    (let [cookies       (:cookies request)]
        (log/info "Incoming cookies: " cookies)
        (-> (http-response/response response-html)
            (http-response/content-type "text/html; charset=utf-8"))))

(defn process-front-page
    "Function that prepares data for the front page."
    [request]
    (finish-processing request (html-renderer/render-front-page)))

(defn process-project-list-page
    "Function that prepares data for the page with project list."
    [request]
    (let [projects      (db-interface/read-project-list)]
        (log/info "Projects:" projects)
        (if projects
            (if (seq projects)
                (finish-processing request (html-renderer/render-project-list projects))
                (finish-processing request (html-renderer/render-error-page "Databáze projektů je prázdná")))
            (finish-processing request (html-renderer/render-error-page "Chyba při přístupu k databázi")))))

(defn process-project-page
    "Function that prepares data for the project page."
    [request]
    (let [params       (:params request)
          project-id   (get params "project-id")
          project-info (db-interface/read-project-info project-id)]
          (log/info "Project ID:" project-id)
          (log/info "Project info" project-info)
          (if project-id
              (let [buildings (db-interface/read-building-list project-id)]
                  (log/info "Buildings:" buildings)
                  (if (seq buildings)
                      (finish-processing request (html-renderer/render-building-list project-id project-info buildings))
                      (finish-processing request (html-renderer/render-error-page "Nebyla nalezena žádná budova"))))
              (finish-processing request (html-renderer/render-error-page "Projekt nebyl vybrán")))))

(defn process-building-page
    "Function that prepares data for the page with list of drawings for the selected building."
    [request]
    (let [params        (:params request)
          project-id    (get params "project-id")
          building-id   (get params "building-id")
          project-info  (db-interface/read-project-info project-id)
          building-info (db-interface/read-building-info building-id)]
          (log/info "Project ID:" project-id)
          (log/info "Project info" project-info)
          (log/info "Building ID:" building-id)
          (log/info "Building info" building-info)
          (if building-id
              (let [drawings (db-interface/read-drawing-list building-id)]
                  (log/info "Drawings" drawings)
                  (if (seq drawings)
                      (finish-processing request (html-renderer/render-drawing-list project-id building-id project-info building-info drawings))
                      (finish-processing request (html-renderer/render-error-page "Nebyl nalezen žádný výkres"))))
              (finish-processing request (html-renderer/render-error-page "Budova nebyla vybrána")))))

(defn process-drawing-page
    "Function that prepares data for the page with selected drawing."
    [request]
    (let [params        (:params request)
          project-id    (get params "project-id")
          building-id   (get params "building-id")
          drawing-id    (get params "drawing-id")
          project-info  (db-interface/read-project-info project-id)
          building-info (db-interface/read-building-info building-id)
          drawing-info  (db-interface/read-drawing-info drawing-id)
          rooms         (db-interface/read-room-list drawing-id)]
          (log/info "Project ID:" project-id)
          (log/info "Project info" project-info)
          (log/info "Building ID:" building-id)
          (log/info "Building info" building-info)
          (log/info "Drawing ID:" drawing-id)
          (log/info "Drawing info" drawing-info)
          (log/info "Rooms" rooms)
          (if drawing-id
              (if drawing-info
                  (finish-processing request (html-renderer/render-drawing project-id building-id drawing-id project-info building-info drawing-info rooms))
                  (finish-processing request (html-renderer/render-error-page "Nebyl nalezen žádný výkres")))
              (finish-processing request (html-renderer/render-error-page "Nebyl vybrán žádný výkres")))))

(defn get-api-part-from-uri
    "Get API part (string) from the full URI. The API part string should not starts with /"
    [uri prefix]
    (let [api-part (re-find #"/[^/]*" (subs uri (count prefix)))]
       (if (and api-part (startsWith api-part "/"))
           (subs api-part 1)
           api-part)))

(defn get-api-command
    "Retrieve the actual command from the API call."
    [uri prefix]
    (if uri
        (if (startsWith uri prefix)
            (let [uri-without-prefix (subs uri (count prefix))]
                (if (empty? uri-without-prefix) ; special handler for a call with / only
                    ""
                    (get-api-part-from-uri uri prefix))))))

(defn api-call-handler
    "This function is used to handle all API calls. Three parameters are expected:
     data structure containing HTTP request, string with URI, and the HTTP method."
    [request uri method prefix]
    (if (= uri prefix)
        (rest-api/api-info-handler request)
        (condp = [method (get-api-command uri prefix)]
            [:get  ""]     (rest-api/api-info-handler request)
            [:get  "info"] (rest-api/info-handler request)
            [:put  "drawing-raw-data"] (rest-api/store-drawing-raw-data request)
                           (rest-api/unknown-endpoint request uri)
        )))

(defn gui-call-handler
    "This function is used to handle all GUI calls. Three parameters are expected:
     data structure containing HTTP request, string with URI, and the HTTP method."
    [request uri method]
    (condp = uri
        "/favicon.ico"                (return-file "favicon.ico" "image/x-icon")
        "/bootstrap.min.css"          (return-file "bootstrap.min.css" "text/css")
        "/chainring.css"              (return-file "chainring.css" "text/css")
        "/bootstrap.min.js"           (return-file "bootstrap.min.js" "application/javascript")
        "/"                           (process-front-page request)
        "/project-list"               (process-project-list-page request)
        "/project"                    (process-project-page request)
        "/building"                   (process-building-page request)
        "/drawing"                    (process-drawing-page request)
        ))

(defn handler
    "Handler that is called by Ring for all requests received from user(s)."
    [request]
    (log/info "request URI:   " (:uri request))
    (log/info "configuration: " (:configuration request))
    (let [uri        (:uri request)
          method     (:request-method request)
          api-prefix (config/get-api-prefix request)]
        (if (startsWith uri api-prefix)
            (api-call-handler request uri method api-prefix)
            (gui-call-handler request uri method))))
