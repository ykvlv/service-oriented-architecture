(ns client.db)

(def default-filters
 {:tickets
  {:id {:value "" :shown true}
   :name {:value "" :shown true}
   :x {:value "" :shown true}
   :y {:value "" :shown true}
   :type {:value "" :shown true}
   :eventId  {:value "" :shown true}}

  :events {:id
           {:value "" :shown true}
           :name
           {:value "" :shown true}
           :date
           {:value "" :shown true}
           :minAge
           {:value "" :shown true}
           :eventType
           {:value "" :shown true}}}
 )
(def default-db
  {:active-panel :home-panel
   :toggle-new false
   :toggle-change false
   :paging {:current-page 1
            :page-size 10}
   :event {:sorting {0 {:field :id :sort-order nil}}}
   :ticket {:update-id 1
            :sorting {0 {:field :id :sort-order nil}}
            :edit {:ticket-id 1
                   :path {:name false
                         ;; ...
                          }}}
   :filters default-filters
   :mode :events #_:tickets})

