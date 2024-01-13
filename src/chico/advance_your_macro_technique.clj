(ns chico.advance-your-macro-technique)

(defmacro assert [x]
  (when *assert* ;; check the dynamic var `clojure.core/*assert*` to make sure
                 ;;   assertions are enabled
    (list 'when-not x
          (list 'throw
                (list 'new 'AssertionError
                      (list 'str "Assert failed: "
                            (list 'pr-str (list 'quote x))))))))

(assert (= 1 2))
;=> AssertionError Assert failed: (= 1 2)  user/eval214 (NO_SOURCE_FILE:1)

(assert (= 1 1))
;=> nil

(macroexpand '(assert (= 1 2)))
;=>
;(if
; (= 1 2)
; nil
; (do
;  (throw
;   (new AssertionError (str "Assert failed: " (pr-str '(= 1 2)))))))

(def a 5)

'(1 2 3 4 a)

;=>(1 2 3 4 a)

`(1 2 3 4 a)

;=> (1 2 3 4 chico.advance-your-macro-technique/a)
(list 1 2 3 4 a)
;=> (1 2 3 4 5)

`(1 2 3 ~a 5)

;=> (1 2 3 5 5)

(defmacro assert [x]
  (when *assert*
    `(when-not ~x
       (throw (new AssertionError (str "Assert failed: " (pr-str '~x)))))))

(def b 4)
`(1 2 3 '~b 5)
;=> (1 2 3 (quote 4) 5)

"" "
Aha! so this strange '~b dance gives us a way to quote the result of 
evaluation and plug that into a slot in the syntax-quote expression.

`(1 (quote (unquote b)))

" ""

(def other-nubmers '(3 4 5 4))

`(1 2 3 ~other-nubmers 6)
;=> (1 2 3 (3 4 5 4) 6)

(concat '(1 2 3) other-nubmers '(6 7 8))
;=>(1 2 3 3 4 5 4 6 7 8)
