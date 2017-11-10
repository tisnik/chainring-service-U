(ns chainring-service.core
    (:gen-class))

(require '[ring.adapter.jetty      :as jetty])
(require '[ring.middleware.params  :as http-params])
(require '[ring.middleware.cookies :as cookies])

(require '[clojure.tools.cli       :as cli])
(require '[clojure.tools.logging   :as log])

(require '[chainring-service.server :as server])

(def default-port
    "Default port on which the service accepts all HTTP requests.")

(def app
    "Definition of a Ring-based application behaviour."
    (-> server/handler            ; handle all events
        cookies/wrap-cookies      ; we need to work with cookies
        http-params/wrap-params)) ; and to process request parameters, of course

(defn start-server
    "Start the HTTP server on the specified port."
    [port]
    (log/info "Starting the server at the port: " port)
    (jetty/run-jetty app {:port (read-string port)}))

(defn -main
    "Entry point to the chainring service."
    [& args]
    (let [port             "3000"]
          (start-server    port)))

