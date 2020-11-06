(ns reverse-string
  (:require clojure.string))


(defn reverse-string [s]
  (apply str (into '() s)))


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  (defn reverse-string [s]
    (clojure.string/reverse s))


  (defn reverse-string [s]
    (apply str (reduce conj '() s)))


  ) ;; End of rich comment block
