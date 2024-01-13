(ns chico.mastering-macros-book.chapter-3.no-value)
;;drawback with macros  can't treat them as values

;. For example, we can pass functions as arguments to higher-order functions like map and filter to decouple 
;looping logic from transformation and selection logic. When we use a macro instead of a function, we lose that ability:

(defn square [x]
  (* x x))

(map square (range 10)) ;=> (0 1 4 9 16 25 36 49 64 81)

(defmacro square-macro [x]
  `(* ~x ~x))

(map square-macro (range 10))
; (err) Syntax error compiling at (src/chico/chapter_3/beware.clj:15:1).
; (err) Can't take value of a macro: #'chico.chapter-3.beware/square-macro

;;work around
(map (fn [n] (square-macro n)) (range 10)) ;=> (0 1 4 9 16 25 36 49 64 81)
;This works because when the anonymous function (fn [n] (square n)) gets compiled,
;the square expression gets macroexpanded, to (fn [n] (clojure.core/* n n)).
;And this is a perfectly reasonable function, so we don’t have any problems with the compiler

(defmacro do-multiplication [expression]
  (cons `* (rest expression)))

(do-multiplication (+ 3 4))

(map (fn [x] (do-multiplication x)) ['(+ 3 3) '(+ 2 3)])
; (err) Syntax error macroexpanding do-multiplication at (src/chico/chapter_3/beware.clj:30:14).
; (err) Don't know how to create ISeq from: clojure.lang.Symbol

;So when Clojure tries to macroexpand (do-multiplication x), it can’t do its job, because x is a symbol. 
;And as the error message tells us, Clojure doesn’t know how to turn a symbol into a sequence. At the risk of 
;belaboring the point, macros take code as input–they don’t (and can’t) know what values will be substituted 
;in place of the symbols in the code at runtime.

