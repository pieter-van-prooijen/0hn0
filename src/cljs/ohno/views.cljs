(ns ohno.views
  (:require
   [re-frame.core :as re-frame]
   [ohno.subs :as subs]
   [ohno.events :as events]))

;; Subscribe and deref in one go
(def <sub (comp deref re-frame/subscribe))
(def >evt re-frame/dispatch)

(defn get-color [color]
  (cond
    (number? color) "blue"
    true (name color)))

(defn piece [x y width height color error]
  (let [grid-size (/ 1000 width)
        radius (/ grid-size 2.2)
        grid-x (+ (* x grid-size) (/ grid-size 2))
        grid-y (+ (* y grid-size) (/ grid-size 2))
        fill-color (get-color color)]
    [:g {:key (str x "-" y)}
     [:circle {:on-click (fn [_]
                           (>evt [::events/toggle-color [x y]]))
               :cx grid-x :cy grid-y :r radius
               :fill fill-color
               :stroke (if error "black" fill-color)
               :stroke-width "6px"}]
     (when (number? color) [:text.large {:x grid-x :y grid-y :dx "-30px" :dy "30px" :fill "white"
                                         :style {:font "bold 100px sans-serif"}} (str color)])]))

(defn board []
  (let [width (<sub [::subs/width])
        height  (<sub [::subs/height])
        pieces (<sub [::subs/board])
        completed? (<sub [::subs/completed?])
        wrong-pieces (<sub [::subs/wrong-pieces])]
    [:svg {:width "50%" :height "50%" :viewBox "0 0 1000 1000"}
     (into [:<>]  ; fragment
           (map (fn [[[x y] color]]
                  [piece x y width height color (and completed? (wrong-pieces [x y]))]) pieces))]))

(defn success []
  (let [success? (<sub [::subs/success?])]
    (if (nil? success?)
      nil
      [:h2.title (if success? "Success!" "Failure!")])))

(defn new-button [size]
  [:a.button {:href "#" :on-click (fn [_] (>evt [::events/generate-board size]))}
   (str size " x " size)])

(defn new-buttons []
  (map (fn [size] [new-button size]) (range 3 8)))

(defn main-panel []
  [:div.container
   [:h1.title "0hn0"]
   [:div
    (into [:<>] (new-buttons))]
   [success]
   [board]])
