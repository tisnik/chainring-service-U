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
    "Module that contains functions used to render HTML pages sent back to the browser.")

(require '[hiccup.core  :as hiccup])
(require '[hiccup.page  :as page])
(require '[hiccup.form  :as form])

(defn render-html-header
    "Renders part of HTML page - the header."
    [url-prefix]
    [:head
        [:title "Chainring"]
        [:meta {:name "Author"    :content "Pavel Tisnovsky"}]
        [:meta {:name "Generator" :content "Clojure"}]
        [:meta {:http-equiv "Content-type" :content "text/html; charset=utf-8"}]
        (page/include-css (str url-prefix "bootstrap.min.css"))
        (page/include-css (str url-prefix "chainring.css"))
        (page/include-js  (str url-prefix "bootstrap.min.js"))
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
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

(defn color-box
    [html-color value]
    [:a {:href "#" :onclick
        (str "document.getElementById('selected-room-color').value='" value "';"
             "document.getElementById('selected-room-color-code').value='" html-color "'")}
        [:div {:class "color-box" :style (str "background-color:" html-color "; display:inline-block")}]])

(defn render-settings-page
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
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th ""]]
                    (for [project projects]
                            [:tr [:td (:id project)]
                                 [:td [:a {:href (str "project?project-id=" (:id project))}(:name project)]]
                                 [:td (:aoid project)]
                                 [:td (:created project)]
                                 [:td [:a {:title "Podrobnější informace o areálu"
                                           :href (str "project-info?project-id=" (:id project))}
                                           [:img {:src "info.gif"}]]]])
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
                    [:tr [:th "AOID"] [:td (:aoid project-info)]]
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
                    [:tr [:th "AOID"] [:td (:aoid building-info)]]
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
                    [:tr [:th "ID"] [:td floor-id]]
                    [:tr [:th "Jméno"] [:td (:name floor-info)]]
                    [:tr [:th "AOID"] [:td (:aoid floor-info)]]
                    [:tr [:th "Vytvořeno"] [:td (:created floor-info)]]
                    [:tr [:th "Modifikováno"] [:td (:modified floor-info)]]
                    [:tr [:th "Počet verzí výkresů"] [:td (get drawing-count :cnt "nelze zjistit")]]
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
                                           [:img {:src "info.gif"}]]]])
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
                        [:th "AOID"]   [:td (:aoid project-info)]
                        [:td [:a {:title "Podrobnější informace o areálu"
                                  :href (str "project-info?project-id=" project-id)}
                                  [:img {:src "info.gif"}]]]]
                    [:tr
                        [:th "Budova"] [:td (:name building-info)]
                        [:th "AOID"]   [:td (:aoid building-info)]
                        [:td [:a {:title "Podrobnější informace o budově"
                                  :href (str "building-info?building-id=" building-id)}
                                  [:img {:src "info.gif"}]]]]
                ]
                [:br]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Podlaží"]
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th "Výkresů"]]
                    (for [floor floors]
                            [:tr [:td (:id floor)]
                                 [:td [:a {:href (str "floor?project-id=" project-id "&building-id=" building-id "&floor-id=" (:id floor))} (:name floor)]]
                                 [:td (:aoid floor)]
                                 [:td (:created floor)]
                                 [:td (:drawings floor)]
                                 [:td [:a {:title "Podrobnější informace o podlaží"
                                           :href (str "floor-info?floor-id=" (:id floor))}
                                           [:img {:src "info.gif"}]]]])
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
                        [:th "AOID"]   [:td (:aoid project-info)]
                        [:td [:a {:title "Podrobnější informace o areálu"
                                  :href (str "project-info?project-id=" project-id)}
                                  [:img {:src "info.gif"}]]]]
                    [:tr
                        [:th "Budova"] [:td (:name building-info)]
                        [:th "AOID"]   [:td (:aoid building-info)]
                        [:td [:a {:title "Podrobnější informace o budově"
                                  :href (str "building-info?building-id=" building-id)}
                                  [:img {:src "info.gif"}]]]]
                    [:tr
                        [:th "Podlaží"] [:td (:name floor-info)]
                        [:th "AOID"]   [:td (:aoid floor-info)]
                        [:td [:a {:title "Podrobnější informace o podlaží"
                                  :href (str "floor-info?floor-id=" building-id)}
                                  [:img {:src "info.gif"}]]]]
                ]
                [:br]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Výkres"]
                         [:th "AOID"]
                         [:th "Vytvořeno"]
                         [:th "Modifikováno"]
                         [:th "Verze"]]
                    (for [drawing drawings]
                            [:tr [:td (:id drawing)]
                                 [:td [:a {:href (str "drawing?project-id=" project-id "&building-id=" building-id "&floor-id=" floor-id "&drawing-id=" (:id drawing))} (:name drawing)]]
                                 [:td (:aoid drawing)]
                                 [:td (:created drawing)]
                                 [:td (:modified drawing)]
                                 [:td (:version drawing)]])
                ]
                [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

(defn render-drawing
    "Render page with drawing."
    [project-id building-id floor-id drawing-id project-info building-info floor-info drawing-info rooms]
    (page/xhtml
        (render-html-header "/")
        [:body {:class "body-drawing"}
            (render-navigation-bar-section "/")
            [:table {:border "1" :style "border-color:#d0d0d0"}
                [:tr [:td {:rowspan 2 :style "vertical-align:top"}
                    [:table {:class "table table-stripped table-hover" :style "width:auto;"}
                        [:tr
                            [:th "Areál"]  [:td (:name project-info)]
                            [:th "AOID"]   [:td (:aoid project-info)]
                            [:td [:a {:title "Podrobnější informace o areálu"
                                      :href (str "project-info?project-id=" project-id)}
                                      [:img {:src "info.gif"}]]]]
                        [:tr
                            [:th "Budova"] [:td (:name building-info)]
                            [:th "AOID"]   [:td (:aoid building-info)]
                            [:td [:a {:title "Podrobnější informace o budově"
                                      :href (str "building-info?building-id=" building-id)}
                                      [:img {:src "info.gif"}]]]]
                        [:tr
                            [:th "Podlaží"] [:td (:name floor-info)]
                            [:th "AOID"]   [:td (:aoid floor-info)]
                            [:td [:a {:title "Podrobnější informace o podlaží"
                                      :href (str "floor-info?floor-id=" building-id)}
                                      [:img {:src "info.gif"}]]]]
                    ]
                    [:table
                        [:tr [:td "Scale:"] [:td [:div "1"]]]
                        [:tr [:td "X-pos:"] [:td [:div "0"]]]
                        [:tr [:td "Y-pos:"] [:td [:div "0"]]]
                    ]
                    [:td {:class "tools"}
                         [:span {:class "tools-spacer"}]
                         [:img {:src "viewmag_plus.gif" :border "0"}] "&nbsp;"
                         [:img {:src "viewmag_minus.gif" :border "0"}] "&nbsp;"
                         [:img {:src "viewmag_1_1.gif" :border "0"}] "&nbsp;"
                         [:img {:src "viewmag_fit.gif" :border "0"}] "&nbsp;"
                         [:span {:class "tools-spacer"}]
                         [:img {:src "arrow1l.gif" :border "0"}] "&nbsp;"
                         [:img {:src "arrow1d.gif" :border "0"}] "&nbsp;"
                         [:img {:src "arrow1u.gif" :border "0"}] "&nbsp;"
                         [:img {:src "arrow1r.gif" :border "0"}] "&nbsp;"
                         [:span {:class "tools-spacer"}]
                         [:img {:src "view_boundary.png" :border "0"}] "&nbsp;"
                         [:img {:src "view_grid.png" :border "0"}] "&nbsp;"
                    ]
                [:tr [:td [:img {:id "drawing" :src "/raster-drawing?command=reset_view"}]]]
            ]]]
            [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
            (render-html-footer)
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
    "Render an ino page with a 'back' button."
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

