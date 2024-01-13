(ns chico.mastering-macros-book.chapter-3.contagious)

(require '[clojure.string :as string])

(defmacro log [& args]
  `(println (str "[INFO] " (string/join " : " ~(vec args)))))

(log "that went well")
; (out) [INFO] that went well
;converting args to a vector instead just use ~args
;args need to be a sequence of things not expression to be evaluate

(defn send [user m]
  (Thread/sleep 1000))

(defn notify [m]
  (apply log m)
  (send "test" m))
; (err) Syntax error compiling at (src/chico/chapter_3/beware/contagious.clj:17:3).
; (err) Can't take value of a macro: #'chico.chapter-3.beware.contagious/log

(def admin-user "test")
(def current-user "test2")

(defmacro notify-everyone [messages]
  `(do
     (send admin-user ~messages)
     (send current-user ~messages)
     (log ~@messages)))

(notify-everyone ["item #1 processed" "by worker #72"])
; (out) [INFO] item #1 processed : by worker #72
