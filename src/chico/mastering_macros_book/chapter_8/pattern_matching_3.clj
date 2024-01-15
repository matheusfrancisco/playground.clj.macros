(ns chico.mastering-macros-book.chapter-8.pattern-matching-3
  (:require
   [clojure.test :refer [deftest is testing]]))

(defn match-item? [matchable-item input]
  (cond
    (symbol? matchable-item) true
    (vector? matchable-item) (and (sequential? input)
                                  (every? identity (map match-item? matchable-item input)))
    :else (= input matchable-item)))

(defn create-test-expression [input match-expression]
  `(and (= (count ~input) ~(count match-expression))
        (every? identity (map match-item? '~match-expression ~input))))

(defn create-bindings-map [input match-expression]
  (let [pairs (map vector match-expression (concat input (repeat nil)))]
    (into {} (filter (fn [[k v]]
                       (not
                        (or (keyword? k)
                            (number? k)
                            (nil? k))))
                     pairs))))

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
                             [0 [y & more]] {:axis "Y" :y y :more more}
                             [[x & more] 0] {:axis "X" :x x :more more}
                             [x y] {:x x :y y}
                             :else :other))]
      (is (= {:axis "Y" :y 5 :more [7 8]} (match-and-bind [0 [5 7 8]])))
      (is (= {:axis "Y" :y 3 :more [4 5]} (match-and-bind [0 [3 4 5]])))
      (is (= {:axis "X" :x 1 :more [2 3]} (match-and-bind [[1 2 3] 0])))
      (is (= {:x 2 :y 0} (match-and-bind [2 0])))
      (is (= {:x 1 :y 2}  (match-and-bind [1 2]))))))

(defn merge [xs ys]
  (loop [acc [] xs xs ys ys]
    (match [(seq xs) (seq ys)]
      [nil b] (concat acc b)
      [a nil] (concat acc a)
      [[x & x-rest] [y & y-rest]] (if (< x y)
                                    (recur (conj acc x) x-rest ys)
                                    (recur (conj acc y) xs y-rest)))))

(pattern-matching)

(merge [1 2 3] [32 4 5])
;(1 2 3 32 4 5)
(merge [1 2 3] [3 4 5])
;(1 2 3 3 4 5)
