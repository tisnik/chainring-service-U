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


(defn render-back-button
    []
    [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"])


(defn render-help-valid-from
    []
    (page/xhtml
        (html-renderer/render-html-header "/")
        [:body
            [:div {:class "container"}
                (html-renderer/render-navigation-bar-section "/")
                [:h1 "Nápověda"]
                [:h3 "Datum platnosti"]
                [:p "Stavební objekty v SAPu mají nastaven datum platnosti, podle kterého je možné určit časový úsek, kdy takový objekt reálně existuje."]
                [:p "Například při zbourání příčky v SAPu zanikne jedna z místností, což se projeví i na výkresech."]
                [:p "Implicitně je datum platnosti nastaveno na aktuální den."]
                [:br]
                (render-back-button)
                [:br][:br][:br][:br]
                (html-renderer/render-html-footer)
            ]]))


(defn render-help-valid-from-settings
    []
    (page/xhtml
        (html-renderer/render-html-header "/")
        [:body
            [:div {:class "container"}
                (html-renderer/render-navigation-bar-section "/")
                [:h1 "Nápověda"]
                [:h3 "Nastavení data platnosti"]
                [:p "Stavební objekty v SAPu mají nastaven datum platnosti, podle kterého je možné určit časový úsek, kdy takový objekt reálně existuje."]
                [:p "Například při zbourání příčky v SAPu zanikne jedna z místností, což se projeví i na výkresech."]
                [:p "Implicitně je datum platnosti nastaveno na aktuální den."]
                [:br]
                (render-back-button)
                [:br][:br][:br][:br]
                (html-renderer/render-html-footer)
            ]]))

