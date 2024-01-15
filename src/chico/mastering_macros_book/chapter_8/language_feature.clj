(ns chico.mastering-macros-book.chapter-8.language-feature
  
  (:require
   [clojure.tools.macro :as macro]))



(defn- default-docstring [name]
  (str "The author carelessly forgot to provide a docstring for `"
       name
       "`. Sorry!"))

(defmacro my-defn [name & body]
  (let [[name args] (macro/name-with-attributes name body)
        name (if (:doc (meta name))
               name
               (vary-meta name assoc :doc (default-docstring name)))]
    `(defn ~name ~@args)))

(my-defn foo [])

(doc foo)
