(ns word-count
  (:require [clojure.string]))

(defn word-count [phrase]
  (frequencies
    (map clojure.string/lower-case
         (re-seq #"\w+" phrase))))
