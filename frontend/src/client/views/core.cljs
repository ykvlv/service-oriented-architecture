(ns client.views.core
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
   [client.myclasses :as cls]
   [client.mycomponents :as components]
   [client.views.tickets :as tickets]
   [client.views.events :as events-view]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

(def sort-selector-values
  {:tickets ["id"
             {:value "name" :desc "Имя"}
             "x"
             "y"
             {:value "creationDate" :desc "Дата создания"}
             {:value "price" :desc "Цена"}
             {:value "discount" :desc "Скидка"}
             {:value "type" :desc "Тип мероприятия"}]

   :events  ["id"
             {:value "name" :desc "Название мероприятия"}
             {:value "date" :desc "Дата мероприятия"}
             {:value "minAge" :desc "Минимальный возраст"}
             {:value "eventType" :desc "Тип ивента"}]})

(defn one-sort [id mode]
  [:div
   [:div {:class (c :flex)}
    [:div {:class (c [:pr 5]
                     [:pt 2]
                     :cursor-pointer)
           :on-click #(dispatch [::events/remove-sorting mode id])}
     "[X]"]
    (components/selector
      (get sort-selector-values mode)
      #(dispatch [::events/change-sort id :field  (.. % -target -value)]))

    (components/selector
      [{:value "netu" :desc "Без сортировки"}
       {:value "asc" :desc "По возрастанию"}
       {:value "desc" :desc "По убыванию"}]
      #(dispatch [::events/change-sort id :sort-order (.. % -target -value)])
      {:default-value "netu"
       :cls
       (c [:px 2] :text-center [:w 35])})]])

(defn sort-view [mode]
  (let [sortings @(subscribe [::subs/sortings mode])]
    [:div
     [:div {:class (c :text-center :font-bold)}
      [:h3 "Сортировки"]
      (when sortings (for [[sort-id _sort-map] sortings]
        ^{:key sort-id}
        [one-sort sort-id mode]))
      (when (< (count sortings) 5)
        [:div {:class [(c :cursor-pointer
                        [:my 2]
                        [:border "#FAFAFA"]
                        :rounded
                        [:bg "#FAFAFA"]
                        :transition-all [:duration 200] :ease-in-out
                        [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                        [:focus :outline-none :shadow-none [:border "#2e3633"]]
                        [:hover [:border "#2e3633"]]) (c :text-center :w-full)]
             :on-click
             #(dispatch [::events/add-sorting mode])}
       [:i.fa-solid.fa-plus]])]]))

(defn filter-view-one [prop only=? label & [selector-values]]
  (let [filter-db @(subscribe [::subs/filters prop])
        shown? (:shown filter-db)]
    ^{:key prop}
    [:div {:class (c :border [:mb 4] [:mt 4] [:p 2])}
     [:div {:class (c :flex :flex-row [:m 1] :justify-center)}
      [:h1 label]]


       [:div {:class (c :w-full)}
        (when (not only=?)
          (components/selector
           ["=" ">" "<" "!="]
           #(dispatch
             [:dispatch-debounce
              {:delay 300
               :event [::events/change-filter
                       prop
                       :operator
                       (.. % -target -value)]}])
           {:default-value "="
            :cls (c :w-full [:mb 2])}))

        (if selector-values
          (components/selector selector-values
                               #(dispatch [::events/change-filter
                                           prop
                                           :value
                                           (.. % -target -value)])                               {:default-value (first selector-values)})
          [:input
           {:class (c
                    :w-full
                    :rounded :border [:h 8])
            :maxLength 30
            :on-change
            #(dispatch [:dispatch-debounce
                        {:delay 300
                         :event [::events/change-filter
                                 prop
                                 :value
                                 (.. % -target -value)]}])
            :placeholder "Фильтр"}])]]))

(defn get-filter-values [mode]
  (let [ticket-types @(subscribe [::subs/ticket-types])
        event-types @(subscribe [::subs/event-types])]
    (get {:tickets [[:id false "id"]
                    [:name true "Имя"]
                    [:coordinateX false "Координата x"]
                    [:coordinateY false "Координата y"]
                    [:creationDate false "Дата создания"]
                    [:price false "Цена"]
                    [:discount false "Скидка"]
                    [:type false "Тип" ticket-types]]
          :events [[:id false "id"]
                   [:name true "Название мероприятия"]
                   [:date false "Дата проведения"]
                   [:minAge false "Минимальный возраст"]
                   [:eventType true "Тип мероприятия"
                    event-types]]} mode)))

(defn filter-view [mode]
  (let [values (get-filter-values mode)]
    [:div
     [:h3 {:class (c :text-center :font-bold)} "Фильтры"]
     (doall
      (for [v values]
        (apply filter-view-one v)))]))

(def button-cls (c :cursor-pointer
                   [:my 2]
                   [:border "#FAFAFA"]
                   :rounded
                   [:bg "#FAFAFA"]
                   :transition-all [:duration 200] :ease-in-out
                   [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                   [:focus :outline-none :shadow-none [:border "#2e3633"]]
                   [:hover [:border "#2e3633"]]))


(defn type-view-2 [type]
  (cond
    (= type "VIP")
    [:i.fa-regular.fa-star]

    (= type "USUAL")
    [:i.fa-solid.fa-check]

    (= type "BUDGETARY")
    [:i.fa-solid.fa-wheelchair-move]

    (= type "CHEAP")
    [:i.fa-regular.fa-star]))

(defn one-ticket-2 [id]
  (let [ticket @(subscribe [::subs/ticket-by-id id])
        event  @(subscribe [::subs/event-by-id (:eventId ticket)])]
    ^{:key id}
    [:div
     [:div {:class [(c [:bg "#FAFAFA"]
                       [:border :current]
                       :flex-row
                       :flex
                       :justify-between
                       [:p 2]
                       [:m 2]
                       [:hover :shadow-inner [:bg :gray-200]]
                       [:rounded :xl])]}
      [:div {:class (c [:w-max 100])}
       [:div
        [:div "ID: " id]
        [:span #_{:class (c :text-sm)}
         (:creationDate ticket) " " (type-view-2 (:type ticket)) " " [:span (:type ticket)] " "]
        [:div  {:class (c [:w-max 100])}
         [:span {:class (c :text-xl :text-bold)}
          [:span.truncate {:class (c [:h 9] [:h-max 9]) :title (:name event)}
           (if (:name event)
             (if (> (count (:name event)) 15)
               (str (apply str (take 15 (:name event))) "...") (:name event))
             "Неизвестное мероприятие")]]]]
       [:span {:class (c :text-sm [:h 9] [:h-max 9])
               :title (:name ticket)}
        (str "Название билета: "
             (if (> (count (:name ticket)) 15)
               (str (apply str (take 15 (:name ticket))) "...") (:name ticket)))]]
      [:div  {:class (c [:w-max 100])}
       [:div
        "СКИДКА: " (:discount ticket) "%"]
       [:div "ЦЕНА: " (:price ticket)]
       [:div (str "(" (:x (:coordinates ticket)) ","  (:y (:coordinates ticket)) ")")]]
      ]]))

(defn header [mode]
  (let [tickets-discount-sum @(subscribe [::subs/tickets-discount-sum])
        tickets-discount-count @(subscribe [::subs/tickets-discount-count])
        tickets-types-count @(subscribe [::subs/tickets-types-count])
        ticket-discount-count-opened? @(subscribe [::subs/ticket-discount-count-opened])]
    [:div {:class (c :flex :flex-col)}
     [:h3 {:class (c :text-center :font-bold)} "Запросы"]
     [:div {:class [(c :flex :flex-col) (c :text-center :w-full)]}

      [:div {:class (c :flex)}
       [:div
        [:button {:class button-cls
                  :on-click
                  #(dispatch [::events/download-ticket-discount-sum])}
         "Билет с минимальным типом"]
        ]
       [:div
        [:button {:class button-cls
                  :on-click
                  #(dispatch [::events/download-ticket-discount-count])}
         "Уникальные типы билетов"]
        (when ticket-discount-count-opened?
          [components/modal
           "Уникальные типы билетов"
           [:div
            [:table {:class [(c :border :table :w-full)
                             cls/div-center]}

             [:thead
              [:tr [:th "Тип билета"]
               [:th "Количество билетов"]]]
             (for [one-map tickets-discount-count]
               ^{:key (:discount one-map)}
               [:tr
                [:td (:discount one-map)]
                [:td (:count one-map)]])]]
           [:button.cancelBtn {:class (c [:w-min 100])
                               :on-click #(dispatch [::events/close-hueta])}
            "Закрыть"]
           :modal-medium])]]
      (if tickets-discount-sum
        [one-ticket-2 (:id tickets-discount-sum) (fn [] (dispatch [::events/delete-ticket (:id tickets-discount-sum)]))]
        nil)
      [:hr {:class (c [:pt 2])}]

      [:div
       [:button {:class button-cls
                 :on-click
                 #(dispatch [::events/download-ticket-types-count])}
        "Получить билеты по подстроке в имени"]
       [:input {:type "text"
                :id "substring-input"
                :class (c
                         :w-full
                         :rounded
                         :border
                         [:h 8])
                :on-change #(dispatch [::events/change-ticket-type (.. % -target -value)])}]

       (if tickets-types-count
         [:div
         (for [ticket tickets-types-count]
           ^{:key (:id ticket)}
           [one-ticket-2 (:id ticket) (fn [] (dispatch [::events/delete-ticket (:id ticket)]))])]
         nil)]]

     [:hr {:class (c [:pt 2])}]
     (sort-view mode)
     [:hr {:class (c [:pt 2])}]
     (filter-view mode)]))

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])
        reloading? @(subscribe [::subs/reloading])]
    [:div {:class (c [:px 15] [:py 2])}
     [:h1 {:class (c :text-center :font-bold)}
      "Дешевый сервис билетов"]
     [:div
      {:class (c :font-mono [:pt 2])}
      [:div
       [:button
        {:on-click #(dispatch [::events/set-mode :tickets])
         :class
         [(when (= :tickets mode) (c :font-bold))

          (c [:px 1] :underline)]}
        "Билеты"]
       [:button
        {:on-click #(dispatch [::events/set-mode :events])
         :class [(when (= :events mode) (c :font-bold))
                 (c [:px 1] :underline)]}
        "Ивенты"]
       [:i.fa-solid.fa-rotate-right.rotate
        {:on-click
         #(dispatch [::events/reload-db])
         :class [(if reloading? :down :down1)
                 (c :cursor-pointer)]}]

       [:div {:class (c :flex)}
        (when (= :events mode)
          [events-view/events-view])
        (when (= :tickets mode)
          [tickets/tickets-view])
        [header mode]
        ]]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])
        all-data? @(re-frame/subscribe [::subs/initialized?])]
    (if-not
     all-data?
      [:div.another-center
       {:style
        {:text-align "center"}}
       [:center
        [:img
         {:src "loading2.gif"}]]]
      [routes/panels @active-panel])))
