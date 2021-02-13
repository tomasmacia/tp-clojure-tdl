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

(defn hand
  [rank hand-name result]
  (and (= rank (result :rank)) (= hand-name (result :hand)))
  )

(defn hand-and-cards
  [rank hand-name best-cards result]
  (and (hand rank hand-name result) (= best-cards (result :cards)))
  )

(deftest evaluation
  (testing "Straight Flush"
    (is (hand  1 :StraightFlush (evaluate "T♣" "J♣" "Q♣" "K♣" "A♣")))
    (is (hand 10 :StraightFlush (evaluate "A♣" "2♣" "3♣" "4♣" "5♣")))
    )
  (testing "Four of a kind"
    (is (hand 11 :FourOfAKind (evaluate "K♣" "A♣" "A♥" "A♦" "A♣")))
    (is (hand 166 :FourOfAKind (evaluate "3♣" "2♣" "2♥" "2♦" "2♣")))
    )
  (testing "Full House"
    (is (hand 167 :FullHouse (evaluate "K♣" "K♥" "A♥" "A♦" "A♣")))
    (is (hand 322 :FullHouse (evaluate "3♣" "3♥" "2♥" "2♦" "2♣")))
    )
  (testing "Flush"
    (is (hand 323 :Flush (evaluate "9♣" "J♣" "Q♣" "K♣" "A♣")))
    (is (hand 1599 :Flush (evaluate "2♣" "3♣" "4♣" "5♣" "7♣")))
    )
  (testing "Straight"
    (is (hand 1600 :Straight (evaluate "T♣" "J♣" "Q♦" "K♥" "A♣")))
    (is (hand 1609 :Straight (evaluate "A♣" "2♣" "3♦" "4♥" "5♣")))
    )
  (testing "Three of a Kind"
    (is (hand 1610 :ThreeOfAKind (evaluate "Q♣" "K♥" "A♥" "A♦" "A♣")))
    (is (hand 2467 :ThreeOfAKind (evaluate "4♣" "3♥" "2♥" "2♦" "2♣")))
    )
  (testing "Two Pairs"
    (is (hand 2468 :TwoPairs (evaluate "Q♣" "K♣" "K♦" "A♥" "A♣")))
    (is (hand 3325 :TwoPairs (evaluate "4♣" "3♣" "3♦" "2♥" "2♣")))
    )
  (testing "One Pair"
    (is (hand 3326 :OnePair (evaluate "J♣" "Q♦" "K♦" "A♥" "A♣")))
    (is (hand 6185 :OnePair (evaluate "2♣" "2♦" "3♦" "4♥" "5♣")))
    )
  (testing "Highest Card"
    (is (hand 6186 :HighCard (evaluate "9♣" "J♦" "Q♦" "K♥" "A♣")))
    (is (hand 7462 :HighCard (evaluate "2♣" "3♦" "4♦" "5♥" "7♣")))
    )
  )

(deftest evaluation-more-than-5-cards
  (testing "Finds highest straight out of 7 cards"
    (is (hand-and-cards 1600 :Straight '("T♣" "J♣" "Q♦" "K♥" "A♣")
          (evaluate "8♣" "9♦" "T♣" "J♣" "Q♦" "K♥" "A♣"))))
  (testing "Finds highest straight out of 7 cards in reverse order"
    (is (hand-and-cards 1600 :Straight '("A♣" "K♦" "Q♣" "J♣" "T♦")
          (evaluate "A♣" "K♦" "Q♣" "J♣" "T♦" "9♥" "8♣"))))
  (testing "Finds highest straight out of 6 cards"
    (is (hand-and-cards 1600 :Straight '("T♣" "J♣" "Q♦" "K♥" "A♣")
          (evaluate "9♦" "T♣" "J♣" "Q♦" "K♥" "A♣"))))
  )

(deftest normalized-evaluations
  (testing "Straight Flush"
    (is (hand  1 :StraightFlush (evaluate "Tc" "Jc" "Qc" "Kc" "Ac")))
    (is (hand 10 :StraightFlush (evaluate "Ac" "2c" "3c" "4c" "5c")))
    )
  (testing "Four of a kind"
    (is (hand 11 :FourOfAKind (evaluate "kc" "ac" "ad" "ah" "as")))
    (is (hand 166 :FourOfAKind (evaluate "3c" "2♣" "2♥" "2d" "2c")))
    )
  )