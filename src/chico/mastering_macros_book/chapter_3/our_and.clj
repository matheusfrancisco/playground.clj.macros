(ns chico.mastering-macros-book.chapter-3.our-and)

(defmacro our-and
  ([] true)
  ([x] x)
  ([x & next]
   `(if ~x (our-and ~@next) ~x)))

(our-and); true
(our-and true false); false
(our-and true true false); false
(our-and true true true); true
(our-and true true true nil); nil
(our-and 1 2 3)

(our-and (do (println "hi there")
             (= 1 2))
         (= 3 4))

; (out) hi there
; (out) hi there
;false

(macroexpand-1 '(our-and (do (println "hi there")
                             (= 1 2))
                         (= 3 4)))

; => (if
;       (do (println "hi there") (= 1 2))
;       (chico.chapter-3.our-and/our-and (= 3 4))
;       (do (println "hi there") (= 1 2)))

(defmacro our-and-fixed
  ([] true)
  ([x] x)
  ([x & next]
   `(let [arg# ~x]
      (if arg# (our-and-fixed ~@next) arg#))))

(our-and-fixed (do (println "hi there")
                   (= 1 2))
               (= 3 4))
; (out) hi there
;false

;as macro authors, should avoid executing input expressions multiple times
;unless we really, really mean to. But more importantly, it’s that when we 
;write macros, it’s great when we can shield the people who use those macros 
;from having to dig into the macro implementations to see why some surprising 
;thing happens. Our macros are being invited to expand into users’ namespaces,
;and we should appreciate and respect that invitation by making as little of 
;a mess as possible.
