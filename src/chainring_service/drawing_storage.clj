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

(def BINARY_FILE_MAGIC_NUMBER 0x6502)
(def BINARY_FILE_VERSION 1)
(def PADDING (int \ ))

(def timeformatter (new java.text.SimpleDateFormat "yyyy-MM-dd HH:mm:ss"))

(defn string->date
    [data]
    (let [created-as-str (:created data)]
        (-> timeformatter (.parse created-as-str) .getTime)))

(defn write-binary-header
    "Write first four bytes of binary file."
    [fout data]
    (.writeShort fout BINARY_FILE_MAGIC_NUMBER)
    (.writeByte  fout BINARY_FILE_VERSION)
    (.writeByte  fout (:version data)))

(defn write-created
    [fout data]
    (let [created (string->date data)]
        (.writeLong fout created)))

(defn write-counters
    [fout data]
    ; use int (4 bytes) for all counters, it is more than enough
    (.writeInt fout (:entities_count data))
    (.writeInt fout (:rooms_count data))
    (.writeInt fout (count (:scales data))))

(defn write-bounds
    [fout data]
    (let [bounds (:bounds data)]
        (.writeDouble fout (:xmin bounds))
        (.writeDouble fout (:ymin bounds))
        (.writeDouble fout (:xmax bounds))
        (.writeDouble fout (:ymax bounds))))

(defn write-scales
    "4+4+3*8 = 32 bytes per scale."
    [fout data]
    (let [scales (:scales data)]
        (doseq [scale scales]
            (.writeInt fout (:width scale))
            (.writeInt fout (:height scale))
            (.writeDouble fout (:scale scale))
            (.writeDouble fout (:xoffset scale))
            (.writeDouble fout (:yoffset scale)))))

(defn write-entity
    [fout entity]
    (.writeByte fout (int (first (:T entity)))))

(defn write-entities
    [fout data]
    (let [entities (:entities data)]
        (doseq [entity entities]
            (write-entity fout entity))))

(defn write-room
    [fout room]
    (let [polygon (:polygon room)]
        (.writeInt fout (count polygon))))

(defn write-rooms
    [fout data]
    (let [rooms (:rooms data)]
        (doseq [room rooms]
            (write-room fout room))))

(defn store-drawing-as-binary
    [id directory raw-data]
    (let [filename  (format "%s/%05d.bin" directory id)]
        (log/info "Storing into" filename)
        (let [data    (json/read-str raw-data :key-fn keyword)
              fos     (new java.io.FileOutputStream filename)
              fout    (new java.io.DataOutputStream fos)]
              (write-binary-header fout data)
              (write-created       fout data)
              (write-counters      fout data)
              (write-bounds        fout data)
              (write-scales        fout data)
              (write-entities      fout data)
              (write-rooms         fout data))))

(defn store-drawing-as
    [id directory store-format raw-data]
    (condp = store-format
        "json"   (store-drawing-as-json   id directory raw-data)
        "edn"    (store-drawing-as-edn    id directory raw-data)
        "binary" (store-drawing-as-binary id directory raw-data)))
