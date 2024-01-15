(ns chico.mastering-macros-book.chapter-8.pattern-matching
  (:require
   [clojure.test :refer [deftest is testing]]))
;;api 

"" "
(match [x]
  [0] :zero
  [1] :one
  [2] :two
  :else :foo)

(cond
  (= [x] [0]) :zero
  (= [x] [1]) :one
  (= [x] [2]) :two
  :else :foo)

" ""

(defn match-clause [input [m-exp r]]
  (if (= :else m-exp)
    [:else r]
    [`(= ~input ~m-exp)
     r]))

(defmacro match [input & more]
  (let [clauses (partition 2 more)]
    `(cond
       ~@(mapcat
          (partial match-clause input)
          #_(fn [[m-exp r]]
              (if (= :else m-exp)
                [:else r]
                [`(= ~input ~m-exp)
                 r]))
          clauses))))

;; expected outcomes
(deftest pattern-matching
  (testing "matches an int"
    ;; will only compile once we've written `match`
    (let [match-simple-int-input (fn [n]
                                   (match [n]
                                     [0] :zero
                                     [1] :one
                                     [2] :two
                                     :else :other))]
      (is (= :zero (match-simple-int-input 0)))
      (is (= :one (match-simple-int-input 1)))
      (is (= :two (match-simple-int-input 2)))
      (is (= :other (match-simple-int-input 3))))))

