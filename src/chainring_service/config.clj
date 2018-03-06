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


(ns chainring-service.config
    "Configuration loader")

(require '[clj-utils.utils :as utils])
(require '[config-loader.config-loader :as config-loader])

(defn full-prefix
    "Construct full prefix for REST API endpoints (including API version)."
    [configuration]
    (str (-> configuration :api :prefix) "/" (-> configuration :api :version)))

(defn update-configuration
    "Update selected items in the configuration structure."
    [configuration]
    (-> configuration
        (update-in [:config :verbose]          utils/parse-boolean)
        (update-in [:config :pretty-print]     utils/parse-boolean)
        (update-in [:renderer :default_width]  utils/parse-int)
        (update-in [:renderer :default_height] utils/parse-int)
        (assoc-in  [:api    :full-prefix]      (full-prefix configuration))))

(defn load-configuration-from-ini
    "Load configuration from the provided INI file and perform conversions
     on selected items from strings to numeric or Boolean values."
    [ini-file-name]
    (-> (config-loader/load-configuration-file ini-file-name)
        update-configuration))

(defn pretty-print?
    "Read the pretty-print settings (it is used for JSON output etc.)"
    [request]
    (-> request :configuration :config :pretty-print))

(defn get-api-prefix
    "Read prefix for API calls from the configuration passed via
     HTTP request structure (middleware can be used to pass config into it)."
    [request]
    (-> request :configuration :api :prefix))

(defn get-api-version
    "Read prefix for API calls from the configuration passed via
     HTTP request structure (middleware can be used to pass config into it)."
    [request]
    (-> request :configuration :api :version))

(defn get-version
    "Read the version of this service from the configuration passed via
     HTTP request structure (middleware can be used to pass config into it)."
    [request]
    (-> request :configuration :info :version))

(defn get-db-version
    "Read the DB version of this service from the configuration passed via
     HTTP request structure (middleware can be used to pass config into it)."
    [request]
    (-> request :configuration :info :db-version))

(defn get-api-full-prefix
    "Read the DB version of this service from the configuration passed via
     HTTP request structure (middleware can be used to pass config into it)."
    [request]
    (-> request :configuration :api :full-prefix))

(defn verbose?
    "Read the verbose settings (it is used for all outputs that does not went into logs)"
    [request]
    (-> request :configuration :config :verbose))
