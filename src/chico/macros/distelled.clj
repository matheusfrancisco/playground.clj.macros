(ns chico.macros.distelled)


;; functions evaluate their arguments first

(defn foo [x]
  (println "foo called")
  x)

(foo (println "hello"))
; eval (current-form): (foo (println "hello"))
; (out) hello
; (out) foo called

(defmacro bar [x]
  (println "bar called")
  x)

(bar (println "hello"))
; eval (current-form): (bar (println "hello"))
; (out) bar called
; (out) hello

(defn foo-2 []
  (list 1 2 3))

(foo-2) ; => (1 2 3)
(eval (foo-2))
; (err) Execution error (ClassCastException) at chico.macros.distelled/eval9936 (REPL:28).
; (err) class java.lang.Long cannot be cast to class clojure.lang.IFn (java.lang.Long is in module java.base of loader 'bootstrap'; clojure.lang.IFn is in unnamed module of loader 'app')


(defmacro bar-2 []
  (list 1 2 3))

(bar-2)
; (err) Execution error (ClassCastException) at chico.macros.distelled/eval10002 (REPL:36).
; (err) class java.lang.Long cannot be cast to class clojure.lang.IFn (java.lang.Long is in module java.base of loader 'bootstrap'; clojure.lang.IFn is in unnamed module of loader 'app')


(defmacro hello [m]
  (str "Hello " m))

;(hello "xico")
;"Hello xico"

