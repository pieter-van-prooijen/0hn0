(ns ohno.subs
  (:require
   [re-frame.core :as re-frame]
   [ohno.board :as board]))

(re-frame/reg-sub
 ::width
 (fn [db]
   (:width db)))

(re-frame/reg-sub
 ::height
 (fn [db]
   (:height db)))

(re-frame/reg-sub
 ::board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 ::original-board
 (fn [db]
   (:original-board db)))

(re-frame/reg-sub
 ::completed?
 (fn [_ _]
   (re-frame/subscribe [::board]))
 (fn [board _]
   (board/completed? board)))


(re-frame/reg-sub
 ::wrong-pieces
 (fn [_ _]
   [(re-frame/subscribe [::board]) (re-frame/subscribe [::width]) (re-frame/subscribe [::height])])
 (fn [[board width height]]
   (into #{} (board/wrong-pieces board width height))))

;; nil if not yet completed
(re-frame/reg-sub
 ::success?
 (fn [_ _]
   [(re-frame/subscribe [::completed?]) (re-frame/subscribe [::wrong-pieces]) ])
 (fn [[completed? wrong-pieces]]
   (when completed? (empty? wrong-pieces))))
