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

(ns chainring-service.html-renderer
    "Module that contains functions used to render HTML pages sent back to the browser.

    Author: Pavel Tisnovsky")


(require '[hiccup.core  :as hiccup])
(require '[hiccup.page  :as page])
(require '[hiccup.form  :as form])


(defn render-html-header
    "Renders part of HTML page - the header."
    [url-prefix & [options]]
    [:head
        [:title "Chainring"]
        [:meta {:name "Author"    :content "Pavel Tisnovsky"}]
        [:meta {:name "Generator" :content "Clojure"}]
        [:meta {:http-equiv "Content-type" :content "text/html; charset=utf-8"}]
        (page/include-css (str url-prefix "bootstrap/bootstrap.min.css"))
        (page/include-css (str url-prefix "chainring.css"))
        (page/include-js  (str url-prefix "bootstrap/bootstrap.min.js"))
        (if (and options (:drawing-id options))
            [:script (str "var drawing_id = " (:drawing-id options) ";")]
            [:script "var drawing_id = null;"])
        (if (and options (:floor-id options))
            [:script (str "var floor_id = '" (:floor-id options) "';")]
            [:script "var floor_id = null;"])
        (if (and options (:version options))
            [:script (str "var version = '" (:version options) "';")]
            [:script "var version = null;"])
        (if (and options (:raster-drawing-id options))
            [:script (str "var raster_drawing_id = " (:raster-drawing-id options) ";")]
            [:script "var raster_drawing_id = null;"])
        (if (and options (:drawing-name options))
            [:script (str "var drawing_name = '" (:drawing-name options) "';")]
            [:script "var drawing_name = null;"])
        (if (and options (:sap-enabled options))
            [:script "var sap_enabled = true;"]
            [:script "var sap_enabled = false;"])
        (if (and options (:sap-url options))
            [:script (str "var sap_url = '" (:sap-url options) "';")]
            [:script "var sap_url = null;"])
        (if (and options (:include-drawing-js? options))
            (page/include-js (str url-prefix "drawing.js")))
    ] ; head
)


(defn render-html-footer
    "Renders part of HTML page - the footer."
    []
    [:div "<br /><br />Chainring verze 0.1"])


(defn render-navigation-bar-section
    "Renders whole navigation bar."
    [url-prefix]
    [:nav {:class "navbar navbar-inverse navbar-fixed-top" :role "navigation"} ; use navbar-default instead of navbar-inverse
        [:div {:class "container-fluid"}
            [:div {:class "row"}
                [:div {:class "col-md-7"}
                    [:div {:class "navbar-header"}
                        [:a {:href url-prefix :class "navbar-brand"} "Chainring"]
                    ] ; ./navbar-header
                    [:div {:class "navbar-header"}
                        [:ul {:class "nav navbar-nav"}
                            ;[:li (tab-class :whitelist mode) [:a {:href (str url-prefix "whitelist")} "Whitelist"]]
                        ]
                    ]
                ] ; col-md-7 ends
                ;[:div {:class "col-md-3"}
                ;    (render-name-field user-name (remember-me-href url-prefix mode))
                ;]
                ;[:div {:class "col-md-2"}
                ;    [:div {:class "navbar-header"}
                ;        [:a {:href (users-href url-prefix mode) :class "navbar-brand"} "Users"]
                 ;   ] ; ./navbar-header
                ;] ; col ends
            ] ; row ends
        ] ; </div .container-fluid>
]); </nav>


(defn render-front-page
    "Render front page of this application."
    []
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:a {:href "project-list" :class "btn btn-success" :role "button" :style "width:10em"} "Seznam areálů"]
                [:div {:style "height: 10ex"}]
                [:h3 "Další volby"]
                [:a {:href "settings" :class "btn btn-default" :role "button" :style "width:10em"} "Nastavení"]
                [:br]
                [:br]
                [:a {:href "db-stats" :class "btn btn-default" :role "button" :style "width:10em"} "Stav databáze"]
                "&nbsp;"
                [:a {:href "drawings-stats" :class "btn btn-default" :role "button" :style "width:10em"} "Stav výkresů"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn passive-color-box
    "Render 'passive' color box into HTML page."
    [html-color]
    [:div {:class "color-box" :style (str "background-color:" html-color "; display:inline-block")}])


(defn color-box
    "Render 'active' color box into HTML page."
    [html-color value]
    [:a {:href "#" :onclick
        (str "document.getElementById('selected-room-color').value='" value "';"
             "document.getElementById('selected-room-color-code').value='" html-color "'")}
        (passive-color-box html-color)])


(defn render-settings-page
    "Render page with settings/configuration."
    [user-id]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Nastavení"]
                (form/form-to [:post "store-settings"]
                    [:input {:type "hidden" :name "selected-room-color-code" :id "selected-room-color-code" :value "xxx"}]
                    [:fieldset {:class "form-group"}
                        [:label {:for "user-id"} "ID uživatele"]
                        [:input {:type "text" :class "form-control" :style "width:10em" :readonly "readonly" :id "user-id" :name "user-id" :value user-id}]
                    ]
                    [:fieldset {:class "form-group"}
                        [:label {:for "resolution"} "Výchozí velikost půdorysu"]
                        [:br]
                        (form/drop-down "resolution" ["320x240", "400x300", "640x480" "800x600" "1024x768"])
                    ]
                    [:fieldset {:class "form-group"}
                        [:label {:for "selected-room-color"} "Barva výplně vybrané místnosti"]
                        [:br]
                        (color-box "#ff0000" "červená")
                        (color-box "#ff8000" "oranžová")
                        (color-box "#ffff00" "žlutá")
                        (color-box "#80ff80" "zelená")
                        (color-box "#80ffff" "modrozelená")
                        (color-box "#8080ff" "modrá")
                        (color-box "#ff80ff" "fialová")
                        (color-box "#a0a0a0" "šedá")
                        (color-box "#ffffff" "bez výplně")
                        [:input {:type "text" :class "form-control" :style "width:10em" :readonly "readonly" :id "selected-room-color" :name "selected-room-color" :value ""}]
                    ]
                    [:fieldset {:class "form-group"}
                        [:label {:for "selected-room-color"} "Šířka obrysu vybrané místnosti"]
                        [:br]
                        (form/drop-down "pen-width" [["1 pixel" 1] ["2 pixely" 2] ["3 pixely" 3]])
                    ]
                    [:br]
                    [:br]
                    [:button {:type "submit" :class "btn btn-success"} "Uložit nastavení"]
                )
                [:br]
                (form/form-to [:get "/"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

; (form/text-field {:size "40" :class "form-control" :placeholder "Search for word"} "word" (str word))
; (form/drop-down "class" ["N/A" "Noun" "Verb" "Adjective" "Adverb" "Pronoun" "Preposition" "Conjunction" "Determiner" "Exclamation"])]]
; (form/check-box "internal")]]


(defn render-db-statistic-page
    "Render page with database statistic."
    [db-stats]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Stav databáze"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "Tabulka"]                [:th "Počet záznamů"]]
                    [:tr [:td "Areály"]                 [:td (:projects db-stats)]]
                    [:tr [:td "Budovy"]                 [:td (:buildings db-stats)]]
                    [:tr [:td "Podlaží"]                [:td (:floors db-stats)]]
                    [:tr [:td "Místnosti (SAP)"]        [:td (:sap-rooms db-stats)]]
                    [:tr [:td "Výkresy"]                [:td (:drawings db-stats)]]
                    [:tr [:td "Data výkresů"]           [:td (:drawings-data db-stats)]]
                    [:tr [:td "Místnosti na výkresech"] [:td (:drawing-rooms db-stats)]]
                    [:tr [:td "Uživatelé"]              [:td (:users db-stats)]]
                ]
                [:br]
                (form/form-to [:get "/"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-drawings-statistic-page
    "Render page with drawings statistic."
    [drawings-count json-count binary-count]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Stav výkresů"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "Formát"]        [:th "Počet výkresů"] [:th ""]]
                    [:tr [:td "Drw"]           [:td drawings-count] [:td [:a {:href "/drawings-list"} "seznam"]]]
                    [:tr [:td "JSON"]          [:td json-count] [:td [:a {:href "/json-list"} "seznam"]]]
                    [:tr [:td "Binary"]        [:td binary-count] [:td [:a {:href "/binary-list"} "seznam"]]]
                ]
                [:br]
                (form/form-to [:get "/"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-drawings-list
    "Render page with list of drawings."
    [drawings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Seznam výkresů ve formátu Drw"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "Výkres"] [:th "Velikost"]]; [:th {:colspan "2"} "Náhled"]]
                    (for [drawing drawings]
                        [:tr [:td (.getName drawing)]
                             [:td (.length drawing) " B"]
                             ;[:td [:a {:href (str "/drawing-preview?drawing-name=" (.getName drawing))} [:img {:src "icons/draw.png"}]]]
                             ;[:td [:a {:href (str "/raster-preview?drawing-name=" (.getName drawing))} [:img {:src "icons/image.png"}]]]
                    ])
                ]
                [:br]
                (form/form-to [:get "/drawings-stats"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-json-list
    [drawings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Seznam výkresů ve formátu JSON"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "Výkres"] [:th "Velikost"] [:th "Náhled"]]
                    (for [drawing drawings]
                        [:tr [:td (.getName drawing)]
                             [:td (.length drawing) " B"]
                             [:td [:a {:href (str "/raster-preview?drawing-name=" (.getName drawing))} [:img {:src "icons/image.png"}]]]]
                    )
                ]
                [:br]
                (form/form-to [:get "/drawings-stats"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-binary-list
    [drawings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Seznam výkresů v binárním formátu"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "Výkres"] [:th "Velikost"] [:th "Náhled"]]
                    (for [drawing drawings]
                        [:tr [:td (.getName drawing)]
                             [:td (.length drawing) " B"]
                             [:td [:a {:href (str "/raster-preview?drawing-name=" (.getName drawing))} [:img {:src "icons/image.png"}]]]]
                    )
                ]
                [:br]
                (form/form-to [:get "/drawings-stats"]
                    [:button {:type "submit" :class "btn btn-primary"} "Zpět"]
                )
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-project-list
    [projects]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Seznam areálů"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Jméno"]
                         [:th "Vytvořeno"]
                         [:th ""]]
                    (for [project projects]
                            [:tr [:td (:id project)]
                                 [:td [:a {:href (str "project?project-id=" (:id project))}(:name project)]]
                                 [:td (:created project)]
                                 [:td [:a {:title "Podrobnější informace o areálu"
                                           :href (str "project-info?project-id=" (:id project))}
                                           [:img {:src "icons/info.gif"}]]]])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-project-info
    [project-id project-info building-count]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Informace o areálu '" (:name project-info) "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"] [:td project-id]]
                    [:tr [:th "Jméno"] [:td (:name project-info)]]
                    [:tr [:th "Vytvořeno"] [:td (:created project-info)]]
                    [:tr [:th "Modifikováno"] [:td (:modified project-info)]]
                    [:tr [:th "Počet budov"] [:td [:a {:href (str "/project?project-id=" project-id)} (get building-count :cnt "nelze zjistit")]]]
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-building-info
    [building-id building-info floor-count]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Informace o budově '" (:name building-info) "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"] [:td building-id]]
                    [:tr [:th "Jméno"] [:td (:name building-info)]]
                    [:tr [:th "Vytvořeno"] [:td (:created building-info)]]
                    [:tr [:th "Modifikováno"] [:td (:modified building-info)]]
                    [:tr [:th "Počet podlaží"] [:td [:a {:href (str "/building?building-id=" building-id)} (get floor-count :cnt "nelze zjistit")]]]
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-floor-info
    [floor-id floor-info drawing-count]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Informace o podlaží '" (:name floor-info) "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"] [:td floor-id] [:td "&nbsp;"]]
                    [:tr [:th "Jméno"] [:td (:name floor-info)] [:td "&nbsp;"]]
                    [:tr [:th "Vytvořeno"] [:td (:created floor-info)] [:td "&nbsp;"]]
                    [:tr [:th "Modifikováno"] [:td (:modified floor-info)] [:td "&nbsp;"]]
                    [:tr [:th "Počet verzí výkresů"] [:td (get drawing-count :cnt "nelze zjistit")] [:td "&nbsp;"]]
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-drawing-info
    [drawing-id drawing-info]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Informace o výkresu '" drawing-id "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID výkresu"]      [:td drawing-id] [:td "&nbsp;"]]
                    [:tr [:th "Vytvořeno"]       [:td (:created drawing-info)]]
                    [:tr [:th "Verze formátu"]   [:td (:format-version drawing-info)]]
                    [:tr [:th "Počet místností"] [:td (:rooms-count drawing-info)]]
                    [:tr [:th "Celkový počet entit"] [:td (-> drawing-info :entities-count :all)]]
                    [:tr [:td "&nbsp;&nbsp;&nbsp;&nbsp;úseček"]  [:td (-> drawing-info :entities-count :lines)]]
                    [:tr [:td "&nbsp;&nbsp;&nbsp;&nbsp;kružnic"] [:td (-> drawing-info :entities-count :circles)]]
                    [:tr [:td "&nbsp;&nbsp;&nbsp;&nbsp;oblouků"] [:td (-> drawing-info :entities-count :arcs)]]
                    [:tr [:td "&nbsp;&nbsp;&nbsp;&nbsp;textů"]   [:td (-> drawing-info :entities-count :texts)]]
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-building-list
    [project-id project-info buildings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Seznam budov v areálu '" (:name project-info) "'")]
                [:h4 (:aoid project-info)]
                [:br]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Budova"]
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th ""]]
                    (for [building buildings]
                            [:tr [:td (:id building)]
                                 [:td [:a {:href (str "building?project-id=" project-id "&building-id=" (:id building))}
                                          (:name building)]]
                                 [:td (:aoid building)]
                                 [:td (:created building)]
                                 [:td [:a {:title "Podrobnější informace o budově"
                                           :href (str "building-info?building-id=" (:id building))}
                                           [:img {:src "icons/info.gif"}]]]])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-floor-list
    [project-id building-id project-info building-info floors]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Seznam podlaží v areálu '" (:name project-info) "' a budově '" (:name building-info) "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr
                        [:th "Areál"]  [:td (:name project-info)]
                        [:th "AOID"]   [:td (:id project-info)]
                        [:td [:a {:title "Podrobnější informace o areálu"
                                  :href (str "project-info?project-id=" project-id)}
                                  [:img {:src "icons/info.gif"}]]]]
                    [:tr
                        [:th "Budova"] [:td (:name building-info)]
                        [:th "AOID"]   [:td (:id building-info)]
                        [:td [:a {:title "Podrobnější informace o budově"
                                  :href (str "building-info?building-id=" building-id)}
                                  [:img {:src "icons/info.gif"}]]]]
                ]
                [:br]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Podlaží"]
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th "Výkresů"]
                         [:th ""]]
                    (for [floor floors]
                            [:tr [:td (:id floor)]
                                 [:td [:a {:href (str "floor?project-id=" project-id "&building-id=" building-id "&floor-id=" (:id floor))} (:name floor)]]
                                 [:td (:aoid floor)]
                                 [:td (:created floor)]
                                 [:td
                                 (if (zero? (:drawings floor))
                                     [:div {:class "no-drawings"} 0]
                                     [:div {:class "has-drawings"} (:drawings floor)])
                                 ]
                                 [:td [:a {:title "Podrobnější informace o podlaží"
                                           :href (str "floor-info?floor-id=" (:id floor))}
                                           [:img {:src "icons/info.gif"}]]]])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn sap-href
    [room]
    "")


(defn render-room-list-page
    [floor-id floor-info version rooms]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Informace místnostech na podlaží '" (:name floor-info) "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"] [:td floor-id] [:td "&nbsp;"]]
                    [:tr [:th "Jméno"] [:td (:name floor-info)] [:td "&nbsp;"]]
                    [:tr [:th "AOID"] [:td (:aoid floor-info)] [:td "&nbsp;"]]]
                [:h4 "Místnosti"]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Jméno"]
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th "Modifikováno"]
                         [:th "Platnost od"]
                         [:th "Platnost do"]
                         [:th "Kapacita"]
                         [:th "Typ"]
                         [:th "Obsazení"]
                         [:th "Interní/externí"]
                         [:th "Plocha"]
                         [:th ""]]
                    (for [room rooms]
                            [:tr [:td (:id room)]
                                 [:td (:name room)]
                                 [:td [:a {:href (sap-href room)} (:aoid room)]]
                                 [:td (:created room)]
                                 [:td (:modified room)]
                                 [:td (:valid_from room)]
                                 [:td (:valid_to room)]
                                 [:td (:capacity room)]
                                 [:td (:room_type_str room)]
                                 [:td (:occupied_by room)]
                                 [:td (if (= (:occupation room) "I") "interní" "externí")]
                                 [:td (:area room) "m<sup>2</sup>"]
                            ])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-drawing-list
    "Render page with list of drawings."
    [project-id building-id floor-id project-info building-info floor-info drawings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 (str "Výkresy pro areál '" (:name project-info) "', budovu '" (:name building-info) "' a podlaží '" "'")]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr
                        [:th "Areál"]  [:td (:name project-info)]
                        [:th "AOID"]   [:td (:id project-info)]
                        [:td [:a {:title "Podrobnější informace o areálu"
                                  :href (str "project-info?project-id=" project-id)}
                                  [:img {:src "icons/info.gif"}]]]]
                    [:tr
                        [:th "Budova"] [:td (:name building-info)]
                        [:th "AOID"]   [:td (:id building-info)]
                        [:td [:a {:title "Podrobnější informace o budově"
                                  :href (str "building-info?building-id=" building-id)}
                                  [:img {:src "icons/info.gif"}]]]]
                    [:tr
                        [:th "Podlaží"] [:td (:name floor-info)]
                        [:th "AOID"]   [:td (:id floor-info)]
                        [:td [:a {:title "Podrobnější informace o podlaží"
                                  :href (str "floor-info?floor-id=" floor-id)}
                                  [:img {:src "icons/info.gif"}]]]]
                ]
                [:br]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Výkres"]
                         [:th "Vytvořeno"]
                         [:th "Modifikováno"]
                         [:th "Verze"]
                         [:th ""]]
                    (for [drawing drawings]
                            [:tr [:td (:id drawing)]
                                 [:td [:a {:href (str "drawing?project-id=" project-id "&building-id=" building-id "&floor-id=" floor-id "&drawing-id=" (:id drawing))} (:name drawing)]]
                                 [:td (:created drawing)]
                                 [:td (:modified drawing)]
                                 [:td (:version drawing)]
                                 [:td [:a {:title "Podrobnější informace o výkresu"
                                           :href (str "drawing-info?drawing-id=" (:id drawing))}
                                           [:img {:src "icons/info.gif"}]]]])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-floor-info-header
    "Render header for 'Vybrane podlazi'/'Selected floor'."
    []
    [:h4 [:a {:href "#" :onclick "showHideRoomInfo()" :title "Zobrazit či skrýt informace o vybraném podlaží"}
             [:img {:src "icons/1downarrow.gif" :id "show_hide_room_info"}]
             " Vybrané podlaží"]])


(defn render-floor-info-table
    "Render info for 'Vybrane podlazi'/'Selected floor'."
    [project-id building-id floor-id drawing-id project-info building-info floor-info drawing-info]
    [:table {:id "room_info" :class "table table-stripped table-hover" :style "width:auto;"}
        [:tr {:class "vcell"}
            [:th "Areál"]  [:td (:name project-info)]
            [:th "AOID"]   [:td (:id project-info)]
            [:td [:a {:title "Podrobnější informace o areálu"
                      :href (str "project-info?project-id=" project-id)}
                      [:img {:src "icons/info.gif"}]]]
            [:td [:a {:title "Vybrat jiný areál"
                      :href "/project-list"}
                      [:img {:src "icons/view-list-tree.png"}]]]]
        [:tr {:class "vcell"}
            [:th "Budova"] [:td (:name building-info)]
            [:th "AOID"]   [:td (:id building-info)]
            [:td [:a {:title "Podrobnější informace o budově"
                      :href (str "building-info?building-id=" building-id)}
                      [:img {:src "icons/info.gif"}]]]
            [:td [:a {:title "Vybrat jinou budovu"
                      :href (str "/project?project-id=" project-id)}
                      [:img {:src "icons/view-list-tree.png"}]]]]
        [:tr {:class "vcell"}
            [:th "Podlaží"] [:td (:name floor-info)]
            [:th "AOID"]   [:td (:id floor-info)]
            [:td [:a {:title "Podrobnější informace o podlaží"
                      :href (str "floor-info?floor-id=" floor-id)}
                      [:img {:src "icons/info.gif"}]]]
            [:td [:a {:title "Vybrat jiné podlaží"
                      :href (str "/building?project-id=" project-id "&building-id=" building-id)}
                      [:img {:src "icons/view-list-tree.png"}]]]]
        [:tr {:class "vcell"}
            [:th "Výkres"] [:td (:name floor-info)]
            [:th "AOID"]   [:td (:id floor-info)]
            [:td [:a {:title "Podrobnější informace o výkresu"
                      :href (str "drawing-info?drawing-id=" drawing-id)}
                      [:img {:src "icons/info.gif"}]]]
            [:td [:a {:title "Vybrat jiný výkres"
                      :href (str "/floor?project-id=" project-id "&building-id=" building-id "&floor-id=" floor-id)}
                      [:img {:src "icons/view-list-tree.png"}]]]]
    ])


(defn render-room-list-header
    "Render header for list of rooms."
    []
    [:h4 [:a {:href "#" :onclick "showHideRoomList()" :title "Zobrazit či skrýt informace o místnostech"}
             [:img {:src "icons/1downarrow.gif" :id "show_hide_room_list"}]
             " Seznam místností"]])


(defn render-room-list
    "Render list of rooms."
    [rooms]
    [:table {:id "room_list" :class "table table-stripped table-hover" :style "width:auto;"}
        [:tr {:class "vcell"} [:th "Jméno"]
             [:th "AOID"]
             [:th "Platnost<br>od/do"]
             [:th "Typ"]
             [:th "Kapacita/<br>plocha"]
             [:th "Obsazení"]]
        (for [room rooms]
                [:tr {:class "vcell"} [:td (:name room)]
                     [:td [:a {:href "#" :onclick (str "onRoomSelect('" (:id room) "')")} (:id room)]]
                     [:td (:valid_from room) "<br>"
                          (:valid_to room)]
                     [:td (:room_type_str room)]
                     [:td (:capacity room) "<br>"
                          (:area room) "m<sup>2</sup>"]
                     [:td (:occupied_by room) "<br>"
                          (if (= (:occupation room) "I") "interní" "externí")]
                ])
        ; not needed, let's use debugger instead for this old part of code
        ; [:tr [:td "Drawing:"] [:td [:div {:id "drawing-id"} drawing-id]]]
        ; [:tr [:td "Scale:"] [:td [:div {:id "scale"} "1"]]]
        ; [:tr [:td "X-pos:"] [:td [:div {:id "xpos"} "0"]]]
        ; [:tr [:td "Y-pos:"] [:td [:div {:id "ypos"} "0"]]]
    ])


(defn render-filters-header
    "Render header for room filters part."
    []
    [:h4 [:a {:href "#" :onclick "showHideFilters()" :title "Zobrazit či skrýt informace o filtrech"}
             [:img {:src "icons/1downarrow.gif" :id "show_hide_filters"}]
             " Filtry"]])


(defn render-filters
    "Render room filters part."
    []
    [:table {:id "filters" :class "table table-stripped table-hover" :style "width:auto;"}
        [:tr {:class "vcell"}
            [:td "Typ"]
            [:td (form/check-box {:onclick "roomTypeCheckBoxClicked();"} "room-type-checkbox")]
            [:td (passive-color-box "rgb(200,150,100)") "Kancelář" [:br]
                 (passive-color-box "rgb(100,150,200)") "Chodba" [:br]
                 (passive-color-box "rgb(200,140,200)") "Hala" [:br]
                 (passive-color-box "rgb(100,200,200)") "WC" [:br]
                 (passive-color-box "rgb(200,200,100)") "Technická místnost" [:br]
                 ]
        [:tr {:class "vcell"}
            [:td "Kapacita"]
            [:td (form/check-box {:onclick "roomCapacityCheckBoxClicked();"} "room-capacity-checkbox")]
            [:td (passive-color-box "rgb(50,50,50)") "0" [:br]
                 (passive-color-box "rgb(100,100,100)") "1" [:br]
                 (passive-color-box "rgb(150,150,150)") "2" [:br]
                 (passive-color-box "rgb(200,200,200)") "&gt;2" [:br]
            ]
        ]
        [:tr {:class "vcell"}
            [:td "Obsazení"]
            [:td (form/check-box {:onclick "roomOccupationCheckBoxClicked();"} "room-ocupation-checkbox")]
            [:td (passive-color-box "rgb(200,100,100)") "interní" [:br]
                 (passive-color-box "rgb(100,100,200)") "externí" [:br]
            ]
        ]
        [:tr {:class "vcell"}
            [:td "Nájemce"]
            [:td (form/check-box {:onclick "roomOccupiedByCheckBoxClicked();"} "room-occupied-by-checkbox")]
            ]
        ]])


(defn render-view-tools
    "Render all view tools with icons and help."
    []
    [:td {:class "tools"}
         [:span {:class "tools-spacer"}]
         [:a {:href "#" :title "Zvětšit"          :onclick "onViewMagPlusClick()"}  [:img {:src "icons/viewmag_plus.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Zmenšit"          :onclick "onViewMagMinusClick()"} [:img {:src "icons/viewmag_minus.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Původní měřítko"  :onclick "onViewMag11Click()"}    [:img {:src "icons/viewmag_1_1.gif"}]] "&nbsp;"
         ;[:img {:src "icons/viewmag_fit.gif"    :border "0" :onclick "onViewMagFitClick()"}] "&nbsp;"
         [:span {:class "tools-spacer"}]
         [:a {:href "#" :title "Posunout doleva"  :onclick "onArrowLeftClick()"}  [:img {:src "icons/arrow1l.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Posunout dolů"    :onclick "onArrowDownClick()"}  [:img {:src "icons/arrow1d.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Posunout nahoru"  :onclick "onArrowUpClick()"}    [:img {:src "icons/arrow1u.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Posunout doprava" :onclick "onArrowRightClick()"} [:img {:src "icons/arrow1r.gif"}]] "&nbsp;"
         [:a {:href "#" :title "Vycentrovat"      :onclick "onCenterViewClick()"} [:img {:src "icons/center.gif"}]] "&nbsp;"
         [:span {:class "tools-spacer"}]
         [:a {:href "#" :title "Zvýraznit okraje" :onclick "onViewBoundaryClick()"} [:img {:src "icons/view_boundary.png"}]] "&nbsp;"
         [:a {:href "#" :title "Zobrazit mřížku"  :onclick "onViewGridClick()"}     [:img {:src "icons/view_grid.png"}]] "&nbsp;"
         [:a {:href "#" :title "Zobrazit bod výběru"  :onclick "onViewBlip()"}      [:img {:src "icons/view_blip.png"}]]
    ])


(defn render-drawing
    "Render page with drawing on the right side and with configurable toolbar on the left side."
    [configuration project-id building-id floor-id drawing-id project-info building-info floor-info drawing-info rooms sap?]
    (page/xhtml
        (render-html-header "/" {:include-drawing-js? true
                                 :floor-id floor-id
                                 :raster-drawing-id drawing-id
                                 :version "C" ; TODO: how to handle this?
                                 :sap-enabled (and (-> configuration :sap-interface :enabled) sap?)
                                 :sap-url     (-> configuration :sap-interface :url)})
        [:body {:class "body-drawing"}
            (render-navigation-bar-section "/")
            [:table {:border "1" :style "border-color:#d0d0d0"}
                ; 1st row - the whole left toolbar + view tools on the right side
                [:tr
                    [:td {:rowspan 2 :style (if sap? "vertical-align:top;width:20em;" "vertical-align:top;width:50em;" )}
                        (if (not sap?)
                            [:span
                                (render-floor-info-header)
                                (render-floor-info-table project-id building-id floor-id drawing-id project-info building-info floor-info drawing-info)
                                (render-room-list-header)
                                (render-room-list rooms)])
                        (render-filters-header)
                        (render-filters)
                    ]
                    (render-view-tools)
                ; 2nd row - drawing on the right side
                [:tr [:td {:style "vertical-align:top"} [:div {:style "position:relative;"} [:img {:id "drawing"
                                 :src (str "/raster-drawing?drawing-id=" drawing-id "&floor-id=" floor-id "&version=C")
                                 :border "0"
                                 :onclick "onImageClick(this, event)"}]]
                                 ; [:div {:id "sap_href_div" :style "display:none"} "&nbsp;"]
                                 [:div [:strong "Vybraná místnost: "]
                                       [:a {:id "sap_href" :name "sap_href"} [:span {:id "selected_room"} "?"]]]
                                 [:div {:style "height:100ex"} "&nbsp;"]]]
                ;[:tr [:td [:div {:class "canvas" :id "drawing_canvas"}]]]
            ]] ; </tr> </table>
            [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
            (render-html-footer)
        ] ; </body>
))


(defn render-raster-preview
    "Render page with preview of selected drawing.
    The drawing remains static and it won't be possible to click on rooms."
    [drawing-name]
    (page/xhtml
        (render-html-header "/")
        [:body {:class "body-drawing"}
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:img {:id "drawing" :src (str "/raster-drawing?drawing-name=" drawing-name) }]
                [:br]
                [:br]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-error-page
    "Render an error page with a 'back' button."
    [message]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Chyba či neočekávaný stav"]
                [:p {:class "alert alert-danger"} message]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                [:br][:br][:br][:br]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))


(defn render-store-settings-page
    "Render page with setting dialog and a 'back' button."
    []
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:h1 "Nastavení"]
                [:p {:class "alert alert-success"} "Změny byly úspěšně uloženy"]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                [:br][:br][:br][:br]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

