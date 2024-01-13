(ns chico.foo)

(defmacro squares [xs]
  (list 'map '#(* % %) xs))

(squares (range 10))

(macroexpand-1 '(squares []))


(defmacro squares-2 [xs] `(map #(* % %) ~xs))

(macroexpand-1 '(squares-2 []))

(gensym)
