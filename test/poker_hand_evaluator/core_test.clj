(ns poker-hand-evaluator.core-test
  (:require [clojure.test :refer :all]
            [poker-hand-evaluator.core :refer :all]))

(defn- base2-str [x] (Integer/toString (int x) 2))

(deftest deck-basics
  (testing "Contains 52 cards"
    (is (= 52 (count deck))))
  (testing "Cards are standard"
    (is (= (set (for [s '("♣" "♥" "♠" "♦") f '("2" "3" "4" "5" "6" "7" "8" "9" "T" "J" "Q" "K" "A")] (str f s)))
          (set (keys deck)))))
  (testing "K♦ is represented with correct bit pattern"
    (is (= "1000000000000100101100100101" (base2-str (deck "K♦")))))
  (testing "5♠ is represented with correct bit pattern"
    (is (= "10000001001100000111" (base2-str (deck "5♠")))))
  (testing "J♣ is represented with correct bit pattern"
    (is (= "10000000001000100100011101" (base2-str (deck "J♣"))))))

(deftest evaluation
  (testing "Straight Flush"
    (is (= {:rank 1 :hand :StraightFlush} (evaluate '("T♣" "J♣" "Q♣" "K♣" "A♣"))))
    (is (= {:rank 10 :hand :StraightFlush} (evaluate '("A♣" "2♣" "3♣" "4♣" "5♣"))))
    )
  (testing "Four of a kind"
    (is (= {:rank 11 :hand :FourOfAKind} (evaluate '("K♣" "A♣" "A♥" "A♦" "A♣"))))
    (is (= {:rank 166 :hand :FourOfAKind} (evaluate '("3♣" "2♣" "2♥" "2♦" "2♣"))))
    )
  (testing "Full House"
    (is (= {:rank 167 :hand :FullHouse} (evaluate '("K♣" "K♥" "A♥" "A♦" "A♣"))))
    (is (= {:rank 322 :hand :FullHouse} (evaluate '("3♣" "3♥" "2♥" "2♦" "2♣"))))
    )
  (testing "Flush"
    (is (= {:rank 323 :hand :Flush} (evaluate '("9♣" "J♣" "Q♣" "K♣" "A♣"))))
    (is (= {:rank 1599 :hand :Flush} (evaluate '("2♣" "3♣" "4♣" "5♣" "7♣"))))
    )
  (testing "Straight"
    (is (= {:rank 1600 :hand :Straight} (evaluate '("T♣" "J♣" "Q♦" "K♥" "A♣"))))
    (is (= {:rank 1609 :hand :Straight} (evaluate '("A♣" "2♣" "3♦" "4♥" "5♣"))))
    )
  (testing "Three of a Kind"
    (is (= {:rank 1610 :hand :ThreeOfAKind} (evaluate '("Q♣" "K♥" "A♥" "A♦" "A♣"))))
    (is (= {:rank 2467 :hand :ThreeOfAKind} (evaluate '("4♣" "3♥" "2♥" "2♦" "2♣"))))
    )
  (testing "Two Pairs"
    (is (= {:rank 2468 :hand :TwoPairs} (evaluate '("Q♣" "K♣" "K♦" "A♥" "A♣"))))
    (is (= {:rank 3325 :hand :TwoPairs} (evaluate '("4♣" "3♣" "3♦" "2♥" "2♣"))))
    )
  (testing "One Pair"
    (is (= {:rank 3326 :hand :OnePair} (evaluate '("J♣" "Q♦" "K♦" "A♥" "A♣"))))
    (is (= {:rank 6185 :hand :OnePair} (evaluate '("2♣" "2♦" "3♦" "4♥" "5♣"))))
    )
  (testing "Highest Card"
    (is (= {:rank 6186 :hand :HighCard} (evaluate '("9♣" "J♦" "Q♦" "K♥" "A♣"))))
    (is (= {:rank 7462 :hand :HighCard} (evaluate '("2♣" "3♦" "4♦" "5♥" "7♣"))))
    )
  )
