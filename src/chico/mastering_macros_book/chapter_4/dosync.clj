(ns chico.mastering-macros-book.chapter-4.dosync
  (:refer-clojure :exclude [dosync sync]))

(defmacro sync [flags-ignored & body]
  `(. clojure.lang.LockingTransaction
      (runInTransaction (fn [] ~@body))))

(defmacro dosync [& exprs]
  `(sync nil ~@exprs))

(def ant-1 (ref {:id 1 :x 0 :y 9}))

(dosync
 (alter ant-1 update-in [:x] 1)
 )

#_(defmacro future [& body] `(future-call (^{:once true} fn* [] ~@body)))
#_(defn future-call 
  "Takes a function of no args and yields a future object that will
  invoke the function in another thread, and will cache the result and
  return it on all subsequent calls to deref/@. If the computation has
  not yet finished, calls to deref/@ will block, unless the variant
  of deref with timeout is used. See also - realized?."
  {:added "1.1"
   :static true}
  [f]
  (let [f (binding-conveyor-fn f)
        fut (.submit clojure.lang.Agent/soloExecutor ^Callable f)]
    (reify 
     clojure.lang.IDeref 
     (deref [_] (deref-future fut))
     clojure.lang.IBlockingDeref
     (deref
      [_ timeout-ms timeout-val]
      (deref-future fut timeout-ms timeout-val))
     clojure.lang.IPending
     (isRealized [_] (.isDone fut))
     java.util.concurrent.Future
      (get [_] (.get fut))
      (get [_ timeout unit] (.get fut timeout unit))
      (isCancelled [_] (.isCancelled fut))
      (isDone [_] (.isDone fut))
      (cancel [_ interrupt?] (.cancel fut interrupt?)))))
