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

(ns chainring-service.drawings-storage
    "Drawing serialization and deserialization functions.
    
    Author: Pavel Tisnovsky")


(require '[clojure.data.json          :as json])
(require '[clojure.tools.logging      :as log])


(defn store-drawing-as-json
    "Store drawing into a JSON format into selected directory."
    [id directory drawing-in-json]
    (let [filename  (format "%s/%s.json" directory id)]
        (log/info "Storing into" filename)
        (spit filename drawing-in-json)))


(defn store-drawing-as-edn
    "Store drawing into an EDN format into selected directory."
    [id directory drawing-in-json]
    (let [filename  (format "%s/%s.edn" directory id)
          data      (json/read-str drawing-in-json :key-fn keyword)]
        (log/info "Storing into" filename)
        (spit filename data)))

; ---------------------------------------------------------------------------
; binary file part

(def BINARY_FILE_MAGIC_NUMBER 0x6502)
(def BINARY_FILE_VERSION 1)
(def PADDING (int \ ))

(def timeformatter (new java.text.SimpleDateFormat "yyyy-MM-dd HH:mm:ss"))

; TODO move into clj-calendar library

(defn string->date
    "Convert string to date (given the string is in proper format)."
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
    "Return info about when the file was created."
    [fout data]
    (let [created (string->date data)]
        (.writeLong fout created)))

(defn write-counters
    "Write all counters: number of entities, number of rooms, number of scale values."
    [fout data]
    ; use int (4 bytes) for all counters, it is more than enough
    (.writeInt fout (:entities_count data))
    (.writeInt fout (:rooms_count data))
    (.writeInt fout (count (:scales data))))

(defn write-bounds
    "Write drawing bounds (four doubles) into the binary file."
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

(defn write-line
    "Write line entity geometry into the binary file."
    [fout entity]
    (.writeDouble fout (:x1 entity))
    (.writeDouble fout (:y1 entity))
    (.writeDouble fout (:x2 entity))
    (.writeDouble fout (:y2 entity)))

(defn write-circle
    "Write circle entity geometry into the binary file."
    [fout entity]
    (.writeDouble fout (:x entity))
    (.writeDouble fout (:y entity))
    (.writeDouble fout (:r  entity)))

(defn write-arc
    "Write arc entity geometry into the binary file."
    [fout entity]
    (.writeDouble fout (:x entity))
    (.writeDouble fout (:y entity))
    (.writeDouble fout (:r  entity))
    (.writeDouble fout (:a1 entity))
    (.writeDouble fout (:a2 entity)))

(defn write-text
    "Write text entity geometry + the string itself into the binary file."
    [fout entity]
    (.writeDouble fout (:x entity))
    (.writeDouble fout (:y entity))
    (.writeInt    fout (count (:text entity)))
    (.writeBytes  fout (:text entity)))

(defn write-entity
    "Write information about one selected entity into the binary file."
    [fout entity]
    (.writeByte fout (int (first (:T entity))))
    (condp = (:T entity)
        "L" (write-line   fout entity)
        "C" (write-circle fout entity)
        "A" (write-arc    fout entity)
        "T" (write-text   fout entity)))
    ; TODO: polyline
    ; TODO: rooms

(defn write-entities
    "Write all entities into the binary file."
    [fout data]
    (let [entities (:entities data)]
        (doseq [entity entities]
            (write-entity fout entity))))

(defn write-room
    "Write information about one selected room into the binary file."
    [fout room]
    (.writeInt fout (:canvas_id room))
    (.writeInt fout (count (:room_id room)))
    (.writeBytes fout (:room_id room))
    (let [polygon (:polygon room)]
        (.writeInt fout (count polygon))
        (doseq [vertex polygon]
            (.writeDouble fout (first vertex))
            (.writeDouble fout (second vertex)))))

(defn write-rooms
    "Write information about all rooms into the binary file."
    [fout data]
    (let [rooms (:rooms data)]
        (doseq [room rooms]
            (write-room fout room))))

(defn store-drawing-as-binary
    "Store drawing into binary format into selected directory."
    [id directory drawing-in-json]
    (let [filename  (format "%s/%05d.bin" directory id)]
        (log/info "Storing into" filename)
        (let [data    (json/read-str drawing-in-json :key-fn keyword)
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
    "Store drawing in selected format."
    [id directory store-format drawing-in-json]
    (condp = store-format
        "json"   (store-drawing-as-json   id directory drawing-in-json)
        "edn"    (store-drawing-as-edn    id directory drawing-in-json)
        "binary" (store-drawing-as-binary id directory drawing-in-json)))

(defn read-binary-header
    "Read first four bytes of binary file."
    [fin]
    [(.readShort fin)    ; magic number
     (.readByte  fin)    ; file versions
     (.readByte  fin)])  ; version

(defn read-created-time
    "Read info about the creation date of the drawing."
    [fin]
    (let [created-ms (.readLong fin)
          created    (new java.util.Date created-ms)]
         [created-ms created]))

(defn read-counters
    "Read all relevant counters from the binary file."
    [fin]
    [(.readInt fin)    ; entity count
     (.readInt fin)    ; rooms count
     (.readInt fin)])  ; scales

(defn read-bounds
    "Read information about drawing bounds from the binary file."
    [fin]
    {:xmin (.readDouble fin)
     :ymin (.readDouble fin)
     :xmax (.readDouble fin)
     :ymax (.readDouble fin)})

(defn read-scales
    "Read information about scales from the binary file."
    [fin scales-count]
    (for [i (range scales-count)]
        {:width   (.readInt fin)
         :height  (.readInt fin)
         :scale   (.readDouble fin)
         :xoffset (.readDouble fin)
         :yoffset (.readDouble fin)}))

(defn read-line-from-binary
    "Read line entity geometry information from the binary file."
    [fin]
    {:T "L"
     :x1 (.readDouble fin)
     :y1 (.readDouble fin)
     :x2 (.readDouble fin)
     :y2 (.readDouble fin)})

(defn read-circle-from-binary
    "Read circle entity geometry information from the binary file."
    [fin]
    {:T "C"
     :x (.readDouble fin)
     :y (.readDouble fin)
     :r (.readDouble fin)})

(defn read-arc-from-binary
    "Read arc entity geometry information from the binary file."
    [fin]
    {:T "A"
     :x  (.readDouble fin)
     :y  (.readDouble fin)
     :r  (.readDouble fin)
     :a1 (.readDouble fin)
     :a2 (.readDouble fin)})

(defn read-text-from-binary
    "Read text entity geometry information + the string itself from the binary file."
    [fin]
    (let [x (.readDouble fin)
          y (.readDouble fin)
          cnt (.readInt fin)
          byte-array (for [i (range cnt)] (.readByte fin))
          text (apply str (map char byte-array))]
          {:T "T"
           :x x
           :y y
           :text text}))

(defn read-entity-from-binary
    "Read one entity from the binary file."
    [fin]
    (let [entity-type (char (.readByte fin))]
        (condp = entity-type
            \L (read-line-from-binary fin)
            \C (read-circle-from-binary fin)
            \A (read-arc-from-binary fin)
            \T (read-text-from-binary fin))))


(defn read-room-polygon
    "Read polygon (for room) from the binary file."
    [fin vertex-count]
    (for [i (range vertex-count)]
        [(.readDouble fin) (.readDouble fin)]))


(defn read-room-from-binary
    "Read room info from the binary file."
    [fin]
    (let [canvas-id    (.readInt fin)
          id-cnt       (.readInt fin) ; length of string
          byte-array   (for [i (range id-cnt)] (.readByte fin))
          room-id      (apply str (map char byte-array))
          vertex-count (.readInt fin)
          polygon      (read-room-polygon fin vertex-count)]
          {:canvas_id canvas-id
           :room_id   room-id
           :polygon   polygon}))
