(ns chico.mastering-macros-book.chapter-4.context)

(defn log [m]
  (let [timestamp (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mmZ")
                           (java.util.Date.))]
    (println timestamp "[INFO]" m)))

(defn process-events [events]
  (doseq [event events]
    (log (format "Event %s has been processed" (:id event)))))

(let [file (java.io.File. (System/getProperty "user.home") "event-stream.log")]
  (with-open [file (clojure.java.io/writer file :append true)]
    (binding [*out* file]
      (process-events [{:id 1231} {:id 213213}]))))

(defmacro with-out-file [file & body]
  `(with-open [writer# (clojure.java.io/writer ~file :append true)]
     (binding [*out* writer#]
       ~@body)))

(let [file (java.io.File. (System/getProperty "user.home") "event-stream.log")]
  (with-out-file file
    (process-events [{:id 1} {:id 2}])
    (process-events [{:id 3} {:id 5}])))

;;usefull in unit testing
(defmacro with-out-str
  "Evaluates exprs in a context in which *out* is bound to a fresh
  StringWriter.  Returns the string created by any nested printing
  calls."
  {:added "1.0"}
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*out* s#]
       ~@body
       (str s#))))

(defmacro with-in-str-2 [s & body]
  `(with-open [s# (-> (java.io.StringReader. ~s)
                      clojure.lang.LineNumberingPushbackReader.)]
     (binding [*in* s#]
       ~@body)))

(defn join-input-lines [s]
  (print (clojure.string/replace (slurp *in*) "\n" ",")))

(let [result (with-in-str-2 "hellooooo ooo "
               (with-out-str (join-input-lines ",")))]

  (assert (= "hellooooo ooo " result)))


