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

(ns chainring-service.real-sap-interface)

(defn read-areals
    []
    [1 2])

(defn read-buildings
    [areal]
    [1 2 3])

(defn read-floors
    [areal building]
    [1 2 3 4])

(defn read-rooms
    [areal building floor]
    [1 2 3 4 5])
