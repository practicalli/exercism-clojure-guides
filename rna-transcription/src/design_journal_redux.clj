(ns design-journal-redux
  (:require clojure.string))


;; Mapping between DNA to RNA
;; A state transaction or dictionary

;; - `G` -> `C`
;; - `C` -> `G`
;; - `T` -> `A`
;; - `A` -> `U`

;; Starting function definition

(def dna->rna
  "DNA to RNA transcription mappings"
  {\G \C \C \G \T \A \A \U})


(defn to-rna
 "Transcribe each nucleotide from a DNA strand into its RNA complement
   Arguments: string representing DNA strand
   Return: string representing RNA strand"
 [dna]
 (clojure.string/join
  (map #(or (dna->rna %)
            (throw (AssertionError. "Unknown nucleotide")))
   dna)))

(comment
  (to-rna "GATTAG"))


;; Annotation: Ignore lint rules
#_{:clj-kondo/ignore [:redefined-var :clojure-lsp/unused-public-var]}
(comment
  (str "coding experiments")


  ;; How to represent the data?

  (def dna-dictionary
      "Convert DNA to RNA"
      {"G" "C"
       "C" "G"
       "T" "A"
       "A" "U"})


  ;; Write a function to get the value for a given key (dna nucleotide)

  (defn to-rna
     "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
     [nucleotide]
     (get dna-dictionary nucleotide))

  ;; (to-rna "G")

  ;; Refactor

  ;; Extract the funtion it its own definition
  ;; Pass a dictionary to make the function pure / deterministic

  (defn dna->rna
     "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
     [dictionary nucleotide]
     (get dictionary nucleotide))

  (dna->rna dna-dictionary "C")


  ;; Iterate over the whole string

  ;; Example DNA sequence
  "ACGTGGTCTTAA"

  (map
    (fn [character] (clojure.string/lower-case character))
    "ACGTGGTCTTAA")
  ;;("a" "c" "g" "t" "g" "g" "t" "c" "t" "t" "a" "a")

  ;; Now for DNA to RNA

  (map
      (fn [nucleotide] (dna->rna dna-dictionary nucleotide))
      "ACGTGGTCTTAA")
  ;;("U" "G" "C" "A" "C" "C" "A" "G" "A" "A" "U" "U")

  ;; Nil nil nil , oh dear


  ;; Investigation
  ;; - what value is returned from the expressions above?

  ;; Refactor

  (defn dna->rna
     "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
     [dictionary nucleotide]
     (get dictionary (str nucleotide)))


  ;; Is the returned data correct though?

  ;; Update the map expression to return the correct form of result

  (clojure.string/join '("U" "G" "C" "A" "C" "C" "A" "G" "A" "A" "U" "U"))

  ;; (quote ("U" "G" "C" "A" "C" "C" "A" "G" "A" "A" "U" "U"))

  ;; Refactor
  ;; Define the `to-rna` funciton that iterates over the DNA sequence
  ;; Return a single sequence


  (defn to-rna
    "Arguments: string representing DNA strand
     Return: string representing RNA strand"
    [dna]
    (clojure.string/join
     (map #(dna->rna dna-dictionary %)
          dna))

  ;; (fn [nucleotide] (dna->rna dna-dictionary nucleotide))


   ;; Use the test example
   (to-rna "ACGTGGTCTTAA")

   ;; One more test case
   ;; Assertion Error for "XCGFGGTDTTAA"

   ;; throw (AssertionError. "message")

   ;; take our test code

   (map #(dna->rna dna-dictionary %)
         "XCGFGGTDTTAA"))

   ;;(nil "G" "C" nil "C" "C" "A" nil "A" "A" "U" "U")

   ;; Using the underlying error mechinisim isnt strictly neccessary
   ;; but it is part of the unit test

   ;; Accessing a map returns a falsy value that can be used as a condition

   ;; `or` macro will run the first expression,
   ;; if a false value is returned, or runs the next expression


  (or convert-dna
      (throw (new AssertionError "Unknown nucleotide")))
      ;; Assemble experiments into the final code and run tests...
  #_()) ; End of rich comment

;; ---------------------------------------------------------
;; Final Solution

;; (def dna->rna
;;   "DNA to RNA transcription mappings"
;;   {\G \C \C \G \T \A \A \U})
;;
;;
;; (defn to-rna
;;   "Transcribe each nucleotide from a DNA strand into its RNA complement
;;   Arguments: string representing DNA strand
;;   Return: string representing RNA strand"
;;   [dna]
;;   (clojure.string/join
;;    (map #(or (dna->rna %)
;;              (throw (AssertionError. "Unknown nucleotide")))
;;         dna)))

;; NOTE: a map can be used as a function, with a key given as an argument

;; NOTE: the anonymous function has a short form

;; Normal form
(fn [argument1 argument2]
  (str argument1 argument2))

;; Short form
#(str % %2)
