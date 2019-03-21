(ns ohno.events
  (:require
   [re-frame.core :as re-frame]
   [ohno.db :as db]
   [ohno.board :as board]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(defn create-board-db [db width height]
  (let [[original-board display-board] (board/create-boards width height)]
    (-> db
        (assoc :width width)
        (assoc :height height)
        (assoc :original-board original-board)
        (assoc :board display-board))))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            (create-board-db db/default-db 5 5)))


(re-frame/reg-event-db
 ::generate-board
 (fn-traced [db [_ size]]
            (create-board-db db size size)))

(re-frame/reg-event-db
 ::toggle-color
 (fn-traced [db [_ [x y]]]
            (update-in db [:board] (fn [board] (board/toggle-piece board [x y])))))

