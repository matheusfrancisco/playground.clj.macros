(ns chico.mastering-macros-book.chapter-8.error-handle
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
  #_(assert (vector? input) "Match input mus be a vector")
  {:pre [(vector? input)
         (even? (count more))]}
  (let [clauses (partition 2 more)]
    `(cond
       ~@(mapcat (partial match-clause input) clauses))))

(match :foo
  :bar "bar"
  :foo "foo"
  :else "else")

; (err) Assert failed: Match input mus be a vector
; (err) (vector? input)

(match [4 2]
  [1 x] :xy
  [x 0] :xy-0
  :other)
; (err) Unexpected error (AssertionError) macroexpanding match at (src/chico/mastering_macros_book/chapter_8/error_handle.clj:52:1).
; (err) Assert failed: (even? (count more))
