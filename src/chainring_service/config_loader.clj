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

(ns chainring-service.config-loader
    "Configuration file(s) parsing and processing functions.")

(require '[clojure-ini.core :as clojure-ini])
(require '[clojure.tools.logging :as log])

(defn properties->map
    "Convert property entries into map. Keys are converted into proper keywords."
    [properties]
    (into {}
        (for [[k v] properties]
              [(keyword k) v])))

(defn load-property-file
    "Load configuration from the provided property file."
    [file-name]
    (with-open [reader (clojure.java.io/reader file-name)] 
        (let [properties (java.util.Properties.)]
            (.load properties reader)
            (properties->map properties))))

(defn load-configuration-file
    "Load configuration from the provided INI file."
    [file-name]
    (clojure-ini/read-ini file-name :keywordize? true))

