(ns chico.macros.mistakes)

;; not work it need to evaluate the symbols
(defmacro infix [[lt op rt]]
  '(op lt rt))

(defmacro infix-2 [[lt op rt]]
  `(~op ~lt ~rt))

(infix-2 [1 + 2]);=> 3

;; the macros always have to be the first element in the list
;;not work
;;(map infix-2 [[1 + 2] [3 * 4] [5 / 6]])

(defmacro mul-2 [xs]
  `(* 2 ~@xs))

(defn mul [nums]
  (mul-2 nums))
; (err) Syntax error (IllegalArgumentException) compiling at (src/chico/macros/mistakes.clj:20:3).
; (err) Don't know how to create ISeq from: clojure.lang.Symbol

;; always use a syntax quote
