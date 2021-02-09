(ns poker-hand-evaluator.core
  (:use [poker-hand-evaluator.lookup-tables])
  (:use [clojure.math.combinatorics]))

(def suit-details
  "Available suits and the respective bit pattern to be used in the card format"
  {"♠" 0x1000, "♥" 0x2000, "♦" 0x4000, "♣" 0x8000})

(def face-details
  "Available face values, including their assigned prime and their rank to be used in the card format"
  {"2" {:prime 2, :index 0}
   "3" {:prime 3, :index 1}
   "4" {:prime 5, :index 2}
   "5" {:prime 7, :index 3}
   "6" {:prime 11, :index 4}
   "7" {:prime 13, :index 5}
   "8" {:prime 17, :index 6}
   "9" {:prime 19, :index 7}
   "T" {:prime 23, :index 8}
   "J" {:prime 29, :index 9}
   "Q" {:prime 31, :index 10}
   "K" {:prime 37, :index 11}
   "A" {:prime 41, :index 12}})

(defn- card-value
  "Card representation as an integer, based on Kevin Suffecool's specs:

    +--------+--------+--------+--------+
    |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
    +--------+--------+--------+--------+

    p = prime number of rank (deuce=2,trey=3,four=5,five=7,...,ace=41)
    r = rank of card (deuce=0,trey=1,four=2,five=3,...,ace=12)
    cdhs = suit of card
    b = bit turned on depending on rank of card"
  [face suit]
  (let [details (face-details face)
        prime (details :prime)
        face-value (details :index)
        suit-value (suit-details suit)]
    (bit-or prime (bit-shift-left face-value 8) suit-value (bit-shift-left 1 (+ 16 face-value)))
    )
  )

(defn- generate-deck
  "creates map: card name -> card value"
  []
  (let [deck {}]
    (into {} (for [face (keys face-details) suit (keys suit-details)] [(str face suit) (card-value face suit)]))
    )
  )

(def deck
  "The default deck to be used by the evaluator"
  (generate-deck))

(defn- calculate-hand-index
  "The hand index is calculated using:

  (c1 OR c2 OR c3 OR c4 OR c5) >> 16

  This value can be used later to find values in lookup tables."
  [cards]
  (bit-shift-right (apply bit-or cards) 16)
  )

(defn- flush-hand
  "The following expression is used to check if the hand is a flush:

      c1 AND c2 AND c3 AND c4 AND c5 AND 0xF000

   If the expression returns a non-zero value, then we have a flush and can use the lookup table for flushes
   to resolve the hand rank."
  [hand-index card-values]
  (and
    (not= (bit-and (apply bit-and card-values) 0xF000) 0)
    (flush-to-rank hand-index)
    ))

(defn- unique-card-hand
  "Straights or High Card hands are resolved using a specific lookup table to resolve hand with 5 unique cards.
  This lookup will return a hand rank only for straights and high cards (0 for any other hand)."
  [hand-index]
  (let [hand-rank (unique5-to-rank hand-index)]
    (and (not= hand-rank 0) hand-rank)
    )
  )

(defn- other-hands
  "Other hands are all non-flush and non-unique5. We first calculate the prime product of all cards:

  q = (c1 AND 0xFF) * (c2 AND 0xFF) * ... * (c5 AND 0xFF)

  Because the range of q is huge (48-100M+), we use 2 lookup tables: we search the index of q on the first
  and then use this index on the second to find the actual hand rank."
  [card-values]
  (let [q (reduce * (map #(bit-and % 0xFF) card-values))
        q-index (java.util.Collections/binarySearch prime-product-to-combination q)]
    (or (combination-to-rank q-index) false)
    ))

(defn- calculate-hand-rank
  "Uses the following strategies to find the hand rank, in order:
    1. bit masking + lookup table for flush hands
    2. bit masking + lookup table for hands with 5 unique cards
    3. prime multiplying + 2 lookup tables for the remaining hands
   "
  [hand]
  (let [card-values (map deck hand)
        hand-index (calculate-hand-index card-values)]
    (or
      (flush-hand hand-index, card-values)
      (unique-card-hand hand-index)
      (other-hands card-values)
      )
    )
  )

(def ranks
  "Poker ranks and their respective maximum rank"
  {7462 :HighCard
   6185 :OnePair
   3325 :TwoPairs
   2467 :ThreeOfAKind
   1609 :Straight
   1599 :Flush
   322 :FullHouse
   166 :FourOfAKind
   10 :StraightFlush})

(defn- resolve-rank-name
  "Resolves the name of a given rank"
  [hand-rank]
  (ranks (+ hand-rank (first (filter #(>= % 0) (sort (map #(- % hand-rank) (keys ranks)))))))
  )

(defn- evaluate-hand
  "Evaluates a 5-card poker hand, returning a map including its name and rank"
  [& hand]
  (let [hand-rank (calculate-hand-rank hand)
        rank-name (resolve-rank-name hand-rank)]
    {:cards hand :rank hand-rank :hand rank-name})
  )

(defn- highest-rank
  "Finds the highest rank for a list of evaluated hands"
  [evaluated-hands]
  (first (sort #(< (%1 :rank) (%2 :rank)) evaluated-hands))
  )

(defn- evaluate-all-combinations
  "Evaluates all possible 5-card combinations for a hand"
  [hand]
  (map #(apply evaluate-hand %) (combinations hand 5))
  )

(defn evaluate
  "Evaluates a poker hand. If it contains more than 5 cards, it returns the best hand possible"
  [& hand]
  (highest-rank (evaluate-all-combinations hand))
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Hasta aca el proyecto existente, nuevas funcionalidades abajo
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn evaluate-all
  "Evalua una lista de manos de 5 o mas cartas"
  [& hands]
  )

(defn best-hand
  "Devuelve la mejor mano que contenga las cartas provistas"
  [& cards]
  )

