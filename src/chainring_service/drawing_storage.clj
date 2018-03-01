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

(ns chainring-service.drawing-storage)

(require '[clojure.data.json          :as json])
(require '[clojure.tools.logging      :as log])

(defn store-drawing-as-json
    [id directory raw-data]
    (let [filename  (format "%s/%05d.json" directory id)]
        (log/info "Storing into" filename)
        (spit filename raw-data)))

(defn store-drawing-as-edn
    [id directory raw-data]
    (let [filename  (format "%s/%05d.edn" directory id)
          data      (json/read-str raw-data :key-fn keyword)]
        (log/info "Storing into" filename)
        (spit filename data)))

(defn store-drawing-as-binary
    [id directory raw-data]
    (let [filename  (format "%s/%05d.bin" directory id)]
        (log/info "Storing into" filename)
        (let [fos  (new java.io.FileOutputStream filename)
              fout (new java.io.DataOutputStream fos)]
              (.writeByte fout 64))))

(defn store-drawing-as
    [id directory store-format raw-data]
    (condp = store-format
        "json"   (store-drawing-as-json   id directory raw-data)
        "edn"    (store-drawing-as-edn    id directory raw-data)
        "binary" (store-drawing-as-binary id directory raw-data)))
