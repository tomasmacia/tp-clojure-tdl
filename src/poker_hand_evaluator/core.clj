(ns poker-hand-evaluator.core)

(def deck
  "the default deck to be used by the evaluator"
  (generate-deck))

(defn- generate-deck []
  "creates map {card -> bit pattern}"
  (let [deck {}]
    (into {} (for [face (keys face-details) suit (keys suit-details)] [(str face suit) (card-value face suit)]))
    )
  )

(def suit-details
  "available suits and the respective bit pattern to be used in the card format"
  {"♠" 0x1000, "♥" 0x2000, "♦" 0x4000, "♣" 0x8000})

(def face-details
  "available face values, including their assigned prime and their rank to be used in the card format"
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
  "Card representation as an integer, based on Kevin Sufecool's specs:

    +--------+--------+--------+--------+
    |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
    +--------+--------+--------+--------+

    p = prime number of rank (deuce=2,trey=3,four=5,five=7,...,ace=41)
    r = rank of card (deuce=0,trey=1,four=2,five=3,...,ace=12)
    cdhs = suit of card
    b = bit turned on depending on rank of card

   Details in http://www.suffecool.net/poker/evaluator.html
  "
  [face suit]
  (let [details (face-details face)
        prime (details :prime)
        face-value (details :index)
        suit-value (suit-details suit)]
    (bit-or prime (bit-shift-left face-value 8) suit-value (bit-shift-left 1 (+ 16 face-value)))
    )
  )