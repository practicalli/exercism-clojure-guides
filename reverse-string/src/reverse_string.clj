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


  (defn reverse-string [s]
    (let [f (fn [x y] (cons y x))]
      (apply str (reduce f "" s)))
    )

  (reverse-string "hello")

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; removes the need for clojure.string/join
  ;; or apply str

  (reduce #(str %2 %1) "" "hello")


  ) ;; End of rich comment block
