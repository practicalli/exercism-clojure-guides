(comment

;; Mentor Notes
;; Using distinct is good
;; `sort` may not be required
;; Using filter and Character/isLetter could simplify the solution
;; Checking for the count also could simplify things further

  (ns mentor-journal
    (:require [clojure.string :as str]))

  (defn- valid? [sentence]
    (->> sentence
         (re-seq #"\p{IsAlphabetic}")
         (apply str)
         (str/lower-case)
         (sort)
         (distinct)))

  (defn pangram? [sentence]
    (let [alphabet (map char (range (int \a) (inc (int \z))))]
      (cond
        (= (valid? sentence) alphabet) true
        :else                          false))))
