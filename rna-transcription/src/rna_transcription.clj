(ns rna-transcription
  (:require clojure.string))

(def dictionary-dna-rna
  "Convert DNA to RNA"
  {"G" "C"
   "C" "G"
   "T" "A"
   "A" "U"}
  )


(defn convert-nucleotide
  "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
  [dictionary nucleotide]
  (get dictionary (str nucleotide)))


(defn to-rna [dna] ;; <- arglist goes here
  (if (clojure.string/includes? dna "X")
    (throw (AssertionError.))
    (apply str
           (map #(convert-nucleotide dictionary-dna-rna %) dna))))
