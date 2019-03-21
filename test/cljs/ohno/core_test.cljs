(ns ohno.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [ohno.core :as core]
            [ohno.board :as board]))

(deftest remove-zeroes
  (testing "removing zeroes"
    (is (= (board/remove-zeroes {[0 0] 0}) {[0 0] :red}))))
