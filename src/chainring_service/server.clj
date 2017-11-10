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
    [request]
    (let [params        (:params request)
          cookies       (:cookies request)
          drawings      (db-interface/read-drawings)]
        (log/info "Incoming cookies: " cookies)
        (log/trace "Drawings: " drawings)
        (-> (http-response/response (html-renderer/render-front-page drawings))
            (http-response/content-type "text/html"))))

(defn process-front-page
    "Function that prepares data for the front page."
    [request]
    (let [params         (:params request)]
        (finish-processing request)))

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
            "/"                           (process-front-page request))))

