(ns ohno.board)

(defn- create-colors [width height]
  (into {} (for [x (range width)
                 y (range height)]
             [[x y] (rand-nth [:blue :red :blue])])))

(defn colour [piece]
  (if (number? piece) :blue piece))

(defn neighbours [board width height x y step-x step-y]
  (let [end-x (if (neg? step-x) -1 width)
        end-y (if (neg? step-y) -1 height)]
    (count (concat (take-while (fn [x1]
                                 (= (colour (get board [x1 y])) :blue))
                               (range (+ x step-x) end-x step-x))
                   (take-while (fn [y1]
                                 (= (colour (get board [x y1])) :blue))
                               (range (+ y step-y) end-y step-y))))))

(defn all-neighbours [board width height x y]
  (+ (neighbours board width height x y -1 -1)
     (neighbours board width height x y 1 1)))

(defn compute-numbers [board width height]
  (reduce (fn [board1 [x y]]
            (if (= (get board1 [x y]) :blue)
              (assoc board1 [x y] (all-neighbours board width height x y))
              board1))
          board
          (keys board)))

(defn remove-zeroes [board]
  (into {} (map (fn [[coord color-or-number]] (if (zero? color-or-number)
                                                [coord :red]
                                                [coord color-or-number])))
        board))

(defn create-board [width height]
  (->  (create-colors width height)
       (compute-numbers width height)
       (remove-zeroes)))

(defn remove-dots [board]
  (reduce (fn [board [x y]]
            (condp = (rand-nth [:keep :remove :remove-number :remove])
              :remove (assoc board [x y] :lightgrey)
              :remove-number (update-in board [[x y]] (fn [old]
                                                        (if (number? old) :blue :lightgrey)))
              :keep board))
          board
          (keys board)))

(defn toggle-piece [board coord]
  (update-in board [coord] (fn [color]
                             (condp = color
                               :blue :red
                               :red  :lightgrey
                               :lightgrey :blue))))

(defn completed? [board]
  (not-any? (partial = :lightgrey) (vals board)))


;; return a list of coords of numbered pieces which "see" the wrong amount of blue's
(defn wrong-pieces [board width height]
  (->> board
       (filter (comp number? second))
       (remove (fn [[[x y] number]]
                 (= (all-neighbours board width height x y) number)))
       (map first)))

(defn create-boards [width height]
  (let [original-board (create-board width height)]
    [original-board (remove-dots original-board)]))

