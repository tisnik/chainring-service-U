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

(ns chainring-service.sap-interface
    "SAP interface that can be configured to use either real SAP or mocked SAP responses.

    Author: Pavel Tisnovsky")

(require '[chainring-service.config :as config])

(require '[chainring-service.real-sap-interface])
(require '[chainring-service.mocked-sap-interface])


(defn get-sap-namespace
    "Return the SAP interface that is to be used - real one or mocked one."
    [mock-sap-response]
    (if mock-sap-response "chainring-service.mocked-sap-interface"
                          "chainring-service.real-sap-interface"))


(defn call-sap-interface
    "Call selected function from the SAP interface - real one or mocked one."
    [request function & params]
    (let [mock-sap-response (config/mock-sap-response? request)
          sap-namespace     (get-sap-namespace mock-sap-response)]
          (apply (ns-resolve (symbol sap-namespace)
                             (symbol (name function)))
                 params)))
