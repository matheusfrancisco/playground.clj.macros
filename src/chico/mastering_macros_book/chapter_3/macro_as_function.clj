(ns chico.mastering-macros-book.chapter-3.macro-as-function)

; Macros Actually Are Functions! 

(defmacro square [x] `(* ~x ~x))

(fn? @#'square);=> true

(@#'square 2)
; (err) Execution error (ArityException) at chico.chapter-3.beware.macro-as-function/eval9412 (REPL:9).
; (err) Wrong number of args (1) passed to: chico.chapter-3.beware.macro-as-function/square

; Clojure is complaining that we’re passing one argument instead of the right number, but what is the right number of 
;arguments for this function? It turns out this function takes two initial arguments, a form and an environment
;(remember &form and &env from secret macro voodoo?), as well as whatever arguments the macro defines (just one in our case).
;This macro doesn’t happen to care about &form &env, so we can just pass dummy values:

(@#'square nil nil 9); => (clojure.core/* 9 9)

; it is much clearer to use the macroexpand-1
(macroexpand-1 '(square 9));=> (clojure.core/* 9 9)
