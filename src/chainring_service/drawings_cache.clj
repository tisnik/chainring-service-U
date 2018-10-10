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

(ns chainring-service.drawings-cache
    "Cache system for drawings

    Author: Pavel Tisnovsky")

(require '[clojure.tools.logging :as log])


(def max-cache-size 250)

(def drawings
    (atom {}))


(def hit-counters
    (atom {}))


(def access-times
    (atom {}))


(defn inc-hit-counter
    [id]
    (swap! hit-counters assoc id (inc (get @hit-counters id))))


(defn cleanup-least-used-item
    []
    (let [drawing-with-min-time (apply min-key #(val %) @access-times)
          drawing-id (key drawing-with-min-time)]
         (log/info (str "cleaning up " drawing-id))
         (swap! drawings dissoc drawing-id)
         (swap! access-times dissoc drawing-id)
         (swap! hit-counters dissoc drawing-id)))


(defn write
    [id data]
    (log/info (str "writing drawing " id " into cache"))
    (swap! access-times assoc id (System/currentTimeMillis))
    (swap! drawings assoc id data)
    (swap! hit-counters assoc id 0)
    (if (> (count @drawings) max-cache-size)
        (cleanup-least-used-item)))


(defn delete
    [id]
    (log/info (str "deleting drawing " id " from cache"))
    (swap! drawings dissoc id)
    (swap! hit-counters dissoc id))


(defn fetch
    [id]
    (swap! access-times assoc id (System/currentTimeMillis))
    (get @drawings id))


(defn store
    [id data]
    (if-not (fetch id)
        (write id data)
        (inc-hit-counter id)))


(defn cache-size
    []
    (count @drawings))


(defn get-access-times
    []
    @access-times)


(defn get-counters
    []
    @hit-counters)
