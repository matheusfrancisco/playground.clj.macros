(ns chico.macros)

(let [expression (read-string "(+ 1 2 3)")]
  (cons (read-string "*")
        (rest expression)))

(eval *1)

(let [expression (read-string (quote (+ 1 2 3)))]
  (cons (quote *)
        (rest expression)))

(eval *1)
