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


(def drawings
    (atom {}))


(def hit-counters
    (atom {}))


(defn inc-hit-counter
    [id]
    (swap! hit-counters assoc id (inc (get @hit-counters id))))


(defn write
    [id data]
    (log/info (str "writing drawing " id " into cache"))
    (swap! drawings assoc id data)
    (swap! hit-counters assoc id 0))


(defn delete
    [id]
    (swap! drawings dissoc id)
    (swap! hit-counters dissoc id))


(defn fetch
    [id]
    (get @drawings id))


(defn store
    [id data]
    (if-not (fetch id)
        (write id data)
        (inc-hit-counter id)))


(defn cache-size
    []
    (count @drawings))
