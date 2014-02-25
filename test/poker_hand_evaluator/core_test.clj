(ns poker-hand-evaluator.core-test
  (:require [clojure.test :refer :all]
            [poker-hand-evaluator.core :refer :all]))

(defn- base2-str [x] (Integer/toString (int x) 2))

(deftest deck-basics
  (testing "Contains 52 cards"
    (is (= 52 (count deck))))
  (testing "Cards are standard"
    (is (=  (set (for [s '("♣" "♥" "♠" "♦") f '("2" "3" "4" "5" "6" "7" "8" "9" "T" "J" "Q" "K" "A")] (str f s)))
          (set (keys deck)))))
  (testing "K♦ is represented with correct bit pattern"
    (is (= "1000000000000100101100100101" (base2-str (deck "K♦")))))
  (testing "5♠ is represented with correct bit pattern"
    (is (= "10000001001100000111" (base2-str (deck "5♠")))))
  (testing "J♣ is represented with correct bit pattern"
    (is (= "10000000001000100100011101" (base2-str (deck "J♣"))))))
