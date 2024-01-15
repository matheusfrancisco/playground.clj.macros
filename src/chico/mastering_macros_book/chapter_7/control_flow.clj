(ns chico.mastering-macros-book.chapter-7.control-flow)

(defmacro while [test & body]
  `(loop []
     (when ~test
       ~@body
       (recur))))

(def counter (atom 0))

(while (< @counter 10)
  (println @counter)
  (swap! counter inc))

; (out) 0
; (out) 1
; (out) 2
; (out) 3
; (out) 4
; (out) 5
; (out) 6
; (out) 7
; (out) 8
; (out) 9

(def counter (atom 0))

(loop []
  (when (< @counter 10)
    (println @counter)
    (swap! counter inc)
    (recur)))

; (out) 0
; (out) 1
; (out) 2
; (out) 3
; (out) 4
; (out) 5
; (out) 6
; (out) 7
; (out) 8
; (out) 9

(defmacro do-while [test & body]
  `(loop []
     ~@body
     (when ~test (recur))))

(defn play-game [s]
  (let [guess (atom nil)]
    (do-while (not= (str s) (str @guess))
              (print "Guess the secret I'm thinking: ")
              (flush)
              (reset! guess (read-line)))
    (println "You got it!")))


