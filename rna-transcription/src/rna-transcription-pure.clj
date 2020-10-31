(ns rna-transcription-pure
  (:require [clojure.string :as string]))

(defn dna->rna
  "Transcribe each nucleotide from a DNA strand into its RNA complement
  Arguments: string representing DNA strand
  Return: string representing RNA strand"
  [transcription dna]
  (string/join
    (map #(or (transcription %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))

(map)

(comment

  (require '[rna-transcription :refer [dna-nucleotide->rna-nucleotide]])

  (dna->rna dna-nucleotide->rna-nucleotide "GATTAGA")

  (dna->rna dna-nucleotide->rna-nucleotide "hello")
  )

(def dna-complement {\G \C, \C \G, \T \A, \A \U})


(defn to-rna [dna]
  {:pre [(every? dna-complement dna)]}
  (->> dna
       (map dna-complement)
       (apply str)))

(to-rna "X")
