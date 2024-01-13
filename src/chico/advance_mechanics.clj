(ns chico.advance-mechanics
  (:require
   [clojure.pprint :as pprint]))

(defmacro info-about-caller []
  (pprint/pprint {:form &form :env &env})
  `(println "macro was called"))

(info-about-caller)

; (out) {:form (info-about-caller), :env nil}
; (out) macro was called

(let [foo "bar"]
  (info-about-caller))

; (out) {:form (info-about-caller),
; (out)  :env
; (out)  {foo
; (out)   #object[clojure.lang.Compiler$LocalBinding 0x7a5c7521 "clojure.lang.Compiler$LocalBinding@7a5c7521"]}}
; (out) macro was called

(defmacro inspect-caller-locals []
  (->> (keys &env)
       (map (fn [k] [`'~k k]))
       (into {})))

(inspect-caller-locals)

(let [foo "bar"]
  (inspect-caller-locals))

;{foo "bar"}

(defmacro inspect-caller-locals-2 []
  (->> (keys &env)
       (map (fn [k] [(list 'quote k) k]))
       (into {})))

(let [foo "bar"]
  (inspect-caller-locals-2))


;{foo "bar"}

(defmacro inspect-called-form [& arguments]
  {:form (list 'quote &form)})

^{:doc "this is a good stuff"} (inspect-called-form 1 2 3)

;{:form (inspect-called-form 1 2 3)}
(meta (:form *1))
;{:line 49, :column 1, :doc "this is a good stuff"}
;https://github.com/clj-commons/potemkin/blob/master/src/potemkin/collections.clj
