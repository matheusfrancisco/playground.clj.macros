(ns chico.mastering-macros-book.chapter-8.pattern-matching-2
  (:require
   [clojure.test :refer [deftest is testing]]))

(defn match-item? [matchable-item input]
  (if (symbol? matchable-item)
    true
    (= input matchable-item)))

(defn create-test-expression [input match-expression]
  `(and (= (count ~input) ~(count match-expression))
        (every? identity (map match-item? '~match-expression ~input))))

(defn create-bindings-map [input match-expression]
  (let [pairs (map vector match-expression input)]
    (into {} (filter (comp symbol? first) pairs))))

(defn create-result-with-bindings [input match-expression r]
  (let [bindings-map (create-bindings-map input match-expression)]
    `(let [~@(mapcat identity bindings-map)]
       ~r)))

(defn match-clause [input [m-expr r]]
  (if (= :else m-expr)
    [:else r]
    [(create-test-expression input m-expr)
     (create-result-with-bindings input m-expr r)]))

(defmacro match [input & more]
  (let [clauses (partition 2 more)]
    `(cond
       ~@(mapcat (partial match-clause input) clauses))))

;; expected outcomes
(deftest pattern-matching
  (testing "matches an int"
    ;; will only compile once we've written `match`
    (let [match-and-bind (fn [[a b]]
                           (match [a b]
                             [0 y] {:axis "Y" :y y}
                             [x 0] {:axis "X" :x x}
                             [x y] {:x x :y y}
                             :else :other))]
      (is (= {:axis "Y" :y 5} (match-and-bind [0 5])))
      (is (= {:axis "Y" :y 3} (match-and-bind [0 3])))
      (is (= {:axis "X" :x 1} (match-and-bind [1 0])))
      (is (= {:axis "X" :x 2} (match-and-bind [2 0])))
      (is (= {:x 1 :y 2}  (match-and-bind [1 2]))))))

(pattern-matching)
