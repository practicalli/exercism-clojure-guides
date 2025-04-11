(ns pangram
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
      :else                          false)))
