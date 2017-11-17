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

(ns chainring-service.rest-api
    "Handler for all REST API calls.")

(require '[ring.util.response         :as http-response])
(require '[clojure.data.json          :as json])
(require '[clojure.tools.logging      :as log])

