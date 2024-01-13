(ns chico.mastering-macros-book.chapter-4.duplication
  (:require [clojure.java.io :as io]))
; that wrap the code they’re given into a new context for evaluating them. 
;For example, you might want to evaluate a given expression with dynamic
;bindings, within a try/catch block, or where a resource is opened and then cleaned up.

(defn info-to-file [path text]
  (let [file (io/writer path :append true)]
    (try
      (binding [*out* file]
        (println "[INFO]" text))
      (finally
        (.close file)))))


(defn error-to-file [path text]
  (let [file (io/writer path :append true)]
    (try
      (binding [*out* file]
        (println "[ERROR]" text))
      (finally
        (.close file)))))
