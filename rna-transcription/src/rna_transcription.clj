(ns rna-transcription
  (:require
   [clojure.string :as string]
   [clojure.spec.alpha :as spec]))

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
        dna)))

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

  (def dna->rna-map (zipmap "GCTA"
                            "CGAU"))

  dna->rna-map

  (spec/def ::dna string?)

  (defn to-rna [dna]
    (let [rna (keep {\G \C, \C \G, \T \A, \A \U} dna)]
      (or (assert (spec/valid? ::dna rna)) rna))))

(comment
  (def value
    (map inc [1 2 3]))

  ;; , e w or , e r to evaluate a name/symbol
  value

  (map inc [1 2 3]))




