(ns chico.mastering-macros-book.chapter-4.rescuing-errors 
  (:require
   [clojure.test :refer [is]]))

#_(defmacro try-expr [msg form]
  `(try ~(assert-expr msg form)
        (catch Throwable t#
          (do-report {:type :error, :message ~msg,
                      :expected '~form, :actual t#}))))

#_(defmacro is
  ([form] `(is ~form nil))
  ([form msg] `(try-expr ~msg ~form)))



(is (= 1 1))
(is (= 1 (do (throw (Exception. "foo")) 1)))

