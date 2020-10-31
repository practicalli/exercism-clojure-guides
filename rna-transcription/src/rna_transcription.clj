(ns rna-transcription
  (:require [clojure.string :as string]))

(def dna-nucleotide->rna-nucleotide
  "DNA to RNA transcription mappings"
  {\G \C
   \C \G
   \T \A
   \A \U})


(defn to-rna
  "Transcribe each nucleotide from a DNA strand into its RNA complement
  Arguments: string representing DNA strand
  Return: string representing RNA strand"
  [dna]
  (string/join
    (map #(or (dna-nucleotide->rna-nucleotide %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))


;; A pre-condition will throw an assertion error if its expression fails

#_(defn to-rna [dna]
    {:pre [(every? dna-complement dna)]}
    (->> dna
         (map dna-complement)
         (apply str)))

(comment
  (to-rna "GATTAGA")
  ;; => "CUAAUCU"

  (to-rna "GX")


  )
