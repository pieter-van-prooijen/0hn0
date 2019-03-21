(ns fiddle
  (:require [ohno.board :as board]))

(board/remove-zeroes {[0 0] 0})

(def x2 {[0 0] 2,  [0 1] :blue,  [1 0] :blue [1 1] :red})
(board/compute-numbers x2)
(board/all-neighbours x2 2 2 0 0)
(board/wrong-pieces x2  2 2)

(filter (comp number? second) x2)

(board/completed? {[0 0] 1 [1 1] :lightgrey})
