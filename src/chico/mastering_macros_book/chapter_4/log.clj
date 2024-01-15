(ns chico.mastering-macros-book.chapter-4.log)

(defn log [m]
  (let [timestamp (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mmZ")
                           (java.util.Date.))]
    (println timestamp "[INFO]" m)))

;; logs to file

(defn process-events [events]
  (doseq [event events]
    (log (format "Event %s has been processed" (:id event)))))

