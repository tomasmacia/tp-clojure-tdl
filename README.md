# poker-hand-evaluator

A Clojure library designed to evaluate poker hands.

## Usage

TBD

## How it works

The main challenge evaluating poker hands is working through the vast amount of possible combinations of cards a poker hand may contain (e.g. there are 2,598,960 possible 5-card combinations).

Luckily poker doesn't treat each individual combination as a unique hand. It defines **rankings** and inside each there are many hands that have the same value (e.g. a pair of nines 9♥9♣4♠3♦2♥ is equivalent to 9♠9♣4♥3♦2♥).

That allows to define **equivalence classes** to group all possible hands of same value, and reducing the number of possible 5-card combinations to just 7,462 [2].

[1] Poker rankings:

* Straight Flush (SF)
* Four of a Kind (4K)
* Full House (FH)
* Flush (F)
* Straight (S)
* Three of a Kind (3K)
* Two Pair (2P)
* One Pair (1P)
* High Card (HC)

[2] [Five-Card Poker Hands](http://www.suffecool.net/poker/7462.html)

[3] [Cactus Kev's Poker Hand Evaluator](http://www.suffecool.net/poker/evaluator.html)

[4] [Two Plus Two Evaluator](http://www.codingthewheel.com/archives/poker-hand-evaluator-roundup/)
