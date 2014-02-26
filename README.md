# poker-hand-evaluator

A Clojure library designed to evaluate poker hands.

## Usage

```clojure
(use 'poker-hand-evaluator.core)

(evaluate "T♣" "J♣" "Q♣" "K♣" "A♣")
;= {:rank 1, :hand :StraightFlush}
```

## How it works

This implementation is currently based on [Kevin Suffecool's poker hand evaluator](http://www.suffecool.net/poker/7462.html)  (aka Cactus Kev's Poker Hand Evaluator).

A good reference to learn about this and other evaluators is [this blog post](http://www.codingthewheel.com/archives/poker-hand-evaluator-roundup/).
