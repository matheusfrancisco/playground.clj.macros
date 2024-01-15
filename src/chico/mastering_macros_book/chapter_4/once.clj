(ns chico.mastering-macros-book.chapter-4.once)

(let [x :a
      f (^:once fn* [] (println x))]
  (f)
  (f)
  )
; (out) :a
; (out) nil

;he second interesting thing is the {:once true} metadata on the fn*. 
;This is a fairly low-level compiler feature to help us avoid accidental 
;memory leaks. It makes sure that any closed-over locals in the function 
;get cleared after the function is called. This way, Clojure doesn’t have 
;to hold onto those values indefinitely, wrongly thinking that you might call 
;the function again. Anytime we create functions that we know will only be 
;invoked once (or if we at least know we don’t need closed-over locals in the function invocation), 
;it’s a good idea to use (^:once fn* [] ...) instead of the plain (fn [] ...), 
;to avoid leaking memory. ^:keyword-here, by the way, is just a shorthand for the common ^{:keyword-here true}
;pattern for Clojure metadata. 
