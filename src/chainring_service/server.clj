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
    (let [params     (:params request)
          project-id (get params "id")]
          (println "Project id:" project-id)
          (if project-id
              (let [buildings (db-interface/read-buildings project-id)]
                  (println "Buildings:" buildings)
                  (if buildings
                      (finish-processing request (html-renderer/render-building-list project-id buildings))
                      (finish-processing request (html-renderer/render-error-page-no-buildings project-id))))
              (finish-processing request (html-renderer/render-error-page-no-project-id)))))

(defn handler
    "Handler that is called by Ring for all requests received from user(s)."
    [request]
    (log/info "request URI: " (request :uri))
    (let [uri          (request :uri)]
        (condp = uri
            "/favicon.ico"                (return-file "favicon.ico" "image/x-icon")
            "/bootstrap.min.css"          (return-file "bootstrap.min.css" "text/css")
            "/chainring.css"              (return-file "chainring.css" "text/css")
            "/bootstrap.min.js"           (return-file "bootstrap.min.js" "application/javascript")
            "/"                           (process-front-page request)
            "/project"                    (process-project-page request))))
