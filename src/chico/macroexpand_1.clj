(ns chico.macroexpand-1)

(macroexpand-1 '(when (= 1 2) (println "math is broken")))
;=> (if (= 1 2) (do (println "math is broken")))

(macroexpand-1 nil)
;=> nil

(macroexpand-1 '(+ 1 2))
;=> (+ 1 2)

(macroexpand-1 (quote (+ 1 2)))
;=> (+ 1 2)


(defmacro when-falsy [test & body]
  (list 'when (list 'not test)
        (cons 'do body)))

(macroexpand-1 '(when-falsy (= 1 2) (println "hi!")))
;=> (when (not (= 1 2)) (do (println "hi!")))

(macroexpand '(when-falsy (= 1 2) (println "hi!")))

;=> (if (not (= 1 2)) (do (do (println "hi!"))))
