(ns chico.mastering-macros-book.chapter-7.threading-as-fn)

(-> 1
    (+ 2)
    (* 3)
    (+ 4)
    (* 5))
;=> 65

(defn thread-first-fn [x & fns]
  (reduce (fn [acc f] (f acc))
          x
          fns))

(thread-first-fn 1
                 #(+ % 2)
                 #(* % 3)
                 #(+ % 4)
                 #(* % 5))
;=> 65

;; or even:
(defn thread-first-fn' [x & fns]
  ((apply comp (reverse fns)) x))
