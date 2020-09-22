(ns rna-transcription
  (:require clojure.string))


(def dna-nucleotide->rna-nucleotide
  "DNA to RNA transcription mappings"
  {\G \C \C \G \T \A \A \U})

(defn to-rna
  "Transcribe each nucleotide from a DNA strand into its RNA complement
  Arguments: string representing DNA strand
  Return: string representing RNA strand"
  [dna]
  (clojure.string/join
    (map #(or (dna-nucleotide->rna-nucleotide %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))
