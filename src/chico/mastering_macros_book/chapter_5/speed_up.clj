(ns chico.mastering-macros-book.chapter-5.speed-up)
;The fastest code is code that doesn’t need to execute at runtime. In our case, the 
;lowest number of instructions wins! Remember that we can consider macros as not even 
;existing at runtime; this is a clear opportunity for us to speed things up.

;https://github.com/hugoduncan/criterium
#_(defn calculate-estimate [{:keys [optimistic realistic pessimistic]}]
  (let [#_#_{:keys [optimistic realistic pessimistic]}
        (fetch-estimates-from-web-service project-id)
        weighted-average (/ (+ optimistic (* realistic 4) pessimistic) 6)
        std-dev (/ (- pessimistic optimistic) 6)]
    (double (+ weighted-average (* 2 std-dev)))))

#_(calculate-estimate {:optimistic 3 :realistic 5 :pessimistic 8})
;6.833333333333333
