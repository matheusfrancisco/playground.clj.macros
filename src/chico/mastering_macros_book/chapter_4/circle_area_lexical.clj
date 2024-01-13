(ns chico.mastering-macros-book.chapter-4.circle-area-lexical)

(defn circle-area [radius]
  (* Math/PI (* radius radius)))

(circle-area 10 )
;314.1592653589793
