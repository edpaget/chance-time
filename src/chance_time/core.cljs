(ns ^:figwheel-always chance-time.core
    (:require [om.core :as om :include-macros true]
              [om-bootstrap.grid :as g]
              [om-bootstrap.button :as b]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state
  (atom {:users {"1" {:name "Caitlin"
                      :avatar "http://placehold.it/150x150"}} 
         :transactions {"1" {:amount 1000
                             :to "1"
                             :description "Rent"
                             :recur :monthly}}}))

(defn merge-payee
  [state]
  (fn [[_ transaction]]
    (assoc transaction :payee ((:users state) (:to transaction)))))

(defn user
  [{:keys [name avatar]} owner]
  (reify
    om/IRender
    (render [_]
      (dom/div {:className "to"}
               (dom/img {:src avatar})
               (dom/h3 nil name))))
  )

(defn transaction
  [[{:keys [payee description amount]}] owner]
  (reify
    om/IRender
    (render [_]
      (g/row nil
             (g/col {:xs 4 :md 3}
                    (om/build user payee))
             (g/col {:xs 8 :md 6}
                    (dom/p #js {:className "description"} description))
             (g/col {:xs 4 :md 3}
                    (dom/p #js {:className "amount"} amount))))))

(defn app-root
  [app owner]
  (reify
    om/IRender
    (render [_]
      (g/grid nil
              (dom/section #js {:id "transactions"}
                           (g/row nil (dom/h2 nil "Transactions"))
                           (g/row nil
                                  (g/col {:md 8 :id "transaction-feed"}
                                         (om/build-all transaction (map (merge-payee app)
                                                                        (:transactions app)))
                                         (b/button {:bs-style "primary"
                                                    :className "pull-right"}
                                                   "New Transaction"))
                                  (g/col {:md 4 :id "filters"}
                                         (dom/p nil "Filter Controls Here"))))))))

(defn init 
  []
  (om/root app-root app-state {:target (. js/document (getElementById "app"))}))
