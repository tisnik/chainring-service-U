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


(defn valid-from
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


(defn valid-from-settings
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


(defn aoid-areal
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "AOID Areálu"]
                [:p "Jedná se o jednotný identifikátor areálu používaný v SAPu. Tento identifikátor má tvar celého kladného čísla (1, 2, atd.)."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn name-areal
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Jméno areálu"]
                [:p "Jedná se o jméno areálu používané v SAPu. Tvar jména může být prakticky libovolný a není povinný."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn aoid-building
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "AOID Budovy"]
                [:p "Jedná se o jednotný identifikátor budovy používaný v SAPu. Tento identifikátor má tvar celého kladného čísla následovaného tečkou a identifikátorem budovy: A.A1 atd."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn name-building
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Jméno budovy"]
                [:p "Jedná se o jméno budovy používané v SAPu. Tvar jména může být prakticky libovolný a není povinný."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn floor-count-building
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Počet podlaží"]
                [:p "Zjištěný počet podlaží v budově"]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn aoid-floor
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "AOID Podlaží"]
                [:p "Jedná se o jednotný identifikátor podlaží používaný v SAPu. Tento identifikátor má tvar celého kladného čísla následovaného tečkou a identifikátorem budovy a identifikátorem podlaží: 1.A1.1MP atd."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn name-floor
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Jméno podlaží"]
                [:p "Jedná se o jméno podlaží používané v SAPu. Tvar jména může být prakticky libovolný a není povinný."]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

(defn drawing-count-floor
    []
    (page/xhtml
        (widgets/header "/")
        [:body
            [:div {:class "container"}
                (widgets/navigation-bar "/")
                [:h1 "Nápověda"]
                [:h3 "Počet výkresů"]
                [:p "Zjištěný počet výkresů pro podlaží v dané oblasti platnosti"]
                [:br]
                (widgets/back-button)
                [:br][:br][:br][:br]
                (widgets/footer)
            ]]))

