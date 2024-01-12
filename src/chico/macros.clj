(ns chico.macros
  (:refer-clojure :exclude [when cond]))

(let [expression (read-string "(+ 1 2 3)")]
  (cons (read-string "*")
        (rest expression)))

(eval *1)

(let [expression (quote (+ 1 2 3))]
  (cons (quote *)
        (rest expression)))

(eval *1)

(defmacro when [test & body]
  (list 'if test (cons 'do body)))

(when (= 2 (+ 1 1))
  (print "You got")
  (print " the touch!")
  (println))

;expand the macro
(list 'if
      '(= 2 (+ 1 1))
      (cons 'do
            '((print "You got")
              (print " the touch!")
              (println))))

(if (= 2 (+ 1 1))
  (do (print "You got")
      (print " the touch!")
      (println)))

(macroexpand-1
 '(when (= 2 2) (println "2 is 2")))

;= > (if (= 2 2) (do (println "2 is 2")))

;; cond

(defmacro cond
  "Takes a set of test/expr pairs. It evaluates each test one at a
  time.  If a test returns logical true, cond evaluates and returns
  the value of the corresponding expr and doesn't evaluate any of the
  other tests or exprs. (cond) returns nil."
  {:added "1.0"}
  [& clauses]
  (when clauses
    (list 'if (first clauses)
          (if (next clauses)
              (second clauses)
              (throw (IllegalArgumentException.
                       "cond requires an even number of forms")))
          (cons 'clojure.core/cond (next (next clauses))))))


;; expanding, up a ladder rung:
(cond)

;; expanding, up a ladder rung and treating `cond` as a function:

'(when clauses
  (list 'if (first clauses)
        (if (next clauses)
          (second clauses)
          (throw (IllegalArgumentException.
                   "cond requires an even number of forms")))
        (cons 'clojure.core/cond (next (next clauses)))))

;; ascending another ladder rung, treating `when` as a function:

(list 'if 'clauses
  (cons 'do
        '((list 'if (first clauses)
                (if (next clauses)
                  (second clauses)
                  (throw (IllegalArgumentException.
                           "cond requires an even number of forms")))
                (cons 'clojure.core/cond (next (next clauses)))))))


;; descending a rung:
(if nil
  (do (list 'if (first nil)
            (if (next nil)
              (second nil)
              (throw (IllegalArgumentException.
                       "cond requires an even number of forms")))
            (cons 'clojure.core/cond (next (next nil))))))

