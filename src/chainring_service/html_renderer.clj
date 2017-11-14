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
    [:div "<br />"])

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
    [drawings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                (if drawings
                    [:table {:class "table table-stripped table-hover" :style "width:auto"}
                        [:tr [:th "ID"]
                             [:th "Výkres"]]
                        (for [drawing drawings]
                                [:tr [:td (:id drawing)]
                                     [:td (:name drawing)]])
                    ])
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
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Projekt"]]
                    (for [project projects]
                            [:tr [:td (:id project)]
                                 [:td [:a {:href (str "project?id=" (:id project))}(:name project)]]])
                ]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

(defn render-building-list
    [project-id buildings]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:p "Projekt" project-id]
                [:table {:class "table table-stripped table-hover" :style "width:auto"}
                    [:tr [:th "ID"]
                         [:th "Budova"]]
                    (for [building buildings]
                            [:tr [:td (:id building)]
                                 [:td (:name building)]])
                ]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

(defn render-error-page
    "Render error page with a 'back' button."
    [message]
    (page/xhtml
        (render-html-header "/")
        [:body
            [:div {:class "container"}
                (render-navigation-bar-section "/")
                [:div {:class "col-md-10"}
                    [:h2 "Chyba či neočekávaný stav"]
                    [:p {:class "alert alert-danger"} message]
                    [:button {:class "btn btn-primary" :onclick "window.history.back()" :type "button"} "Zpět"]
                ]
                [:br][:br][:br][:br]
                (render-html-footer)
            ] ; </div class="container">
        ] ; </body>
))

