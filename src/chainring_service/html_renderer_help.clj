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

(ns chainring-service.html-renderer-help
    "Module that contains functions used to render HTML pages with help that are sent back to the browser.

    Author: Pavel Tisnovsky")


(require '[hiccup.core  :as hiccup])
(require '[hiccup.page  :as page])
(require '[hiccup.form  :as form])

(require '[chainring-service.html-renderer :as html-renderer])
(require '[chainring-service.html-renderer-widgets :as widgets])


(defn render-help-valid-from
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Datum platnosti"]
                [:p "Stavební objekty v SAPu mají nastaven datum platnosti, podle kterého je možné určit časový úsek, kdy takový objekt reálně existuje."]
                [:p "Například při zbourání příčky v SAPu zanikne jedna z místností, což se projeví i na výkresech."]
                [:p "Implicitně je datum platnosti nastaveno na aktuální den."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))


(defn render-help-valid-from-settings
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Nastavení data platnosti"]
                [:p "Stavební objekty v SAPu mají nastaven datum platnosti, podle kterého je možné určit časový úsek, kdy takový objekt reálně existuje."]
                [:p "Například při zbourání příčky v SAPu zanikne jedna z místností, což se projeví i na výkresech."]
                [:p "Implicitně je datum platnosti nastaveno na aktuální den."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))


(defn render-help-aoid-areal
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "AOID Areálu"]
                [:p ""]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn render-help-name-areal
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Jméno areálu"]
                [:p ""]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

