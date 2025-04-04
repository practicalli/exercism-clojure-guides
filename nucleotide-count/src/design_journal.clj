;; (ns nucleotide-count)
(ns design-journal)

;; Given a single stranded DNA string, compute how many times each nucleotide occurs in the string.

(def valid-nucleotides
  "Characters representing valid nucleotides"
  [\A \C \G \T])

;; Function definition signatures (derived from test code)

(defn count-of-nucleotide-in-strand
  [nucleotide strand]
  (if (some #(= nucleotide %) valid-nucleotides)
    (count
     (filter #(= nucleotide %)
             strand))
    (throw (Throwable.))))

(defn nucleotide-counts
  "Count all nucleotide in a strand"
  [strand]
  (merge {\A 0 \C 0 \G 0 \T 0}
         (frequencies "GGGGGAACCCGG")))

#_{:clj-kondo/ignore [:redefined-var :clojure-lsp/unused-public-var]}
(comment

;; Count the occurances

  "GGGGGTAACCCGG"

  (count
   (filter (fn [nucleotide] (= nucleotide \A))
           "GGGGGTAACCCGG"))

;; Define the data

  (def valid-nucleotides
    "Characters representing valid nucleotides"
    [\A \C \G \T])

;; Exception handling required
  ;;  (throw (Throwable.)) if nucleotide is \X

;; Or use a predicate with some (some element? in the sequence)
  (some #(= \G %) valid-nucleotides)

  (some #{\G} valid-nucleotides)

  (defn count-of-nucleotide-in-strand
    [nucleotide strand]
    (if (some #(= nucleotide %) valid-nucleotides)
      (count
       (filter #(= nucleotide %
                   strand
                   (throw (Throwable.)))))))

  (count-of-nucleotide-in-strand \T "GGGGGTAACCCGG")

;; Design the second function

  ;; How often does a nucleotide appear

  (map
   #(if (= % \A) 1 0)
   valid-nucleotides)

  ;; Add the result to get the total count

;; Is there a more elegant way?

  (filter #(= % \A) valid-nucleotides)

  ;; Count the elements in the returned sequence for the total

;; Design the second function

  ;; How often does a nucleotide appear
  ;; NOTE: zero must be returned when there are no appearences

  ;; Return value always in the form
  {\A 20, \T 21, \G 17, \C 12}

;; Hammock time...

  ;; How often does something appear,
  ;; how frequenct is it?
  ;; Is there a clojure standard library for that (approx 700 functions)
  ;; https://clojure-docs.org/

  (frequencies "GGGGGAACCCGG"

;; If there are missing nucleotides then there is no answer

    ;; What if there is a starting point

               {\A 0 \C 0 \G 0 \T 0})

    ;; Then merge the result of frequencies

  (merge {\A 0 \C 0 \G 0 \T 0}
         (frequencies "GGGGGAACCCGG")

  ;; Update the function definition and run tests

         #_() ; End of rich comment

;; ---------------------------------------------------------
;; Solution

         #_(defn count-of-nucleotide-in-strand [nucleotide strand]
             (if (= \X nucleotide)
               (throw (Throwable.))
               (count (filter #(= nucleotide %) strand))))

         #_(defn nucleotide-counts [strand]
             (apply merge
                    (for [nucleotide strand]
                      {nucleotide
                       (count-of-nucleotide-in-strand nucleotide strand)})))

         #_(defn count-of-nucleotide-in-strand [nucleotide strand]
             (if (some (set nucleotide) valid-nucleotides)
               (count (filter #(= nucleotide %) strand))
               (throw (Throwable.))))))

;; End of Solution
;; ---------------------------------------------------------

(comment)

#_{:clj-kondo/ignore [:redefined-var]}

#_() ; End of rich comment

;; (defn count-of-nucleotide-in-strand [nucleotide strand]
  ;;   (get (frequencies strand) nucleotide (throw (Throwable.))))

;; add a guard condition if passed an empty strand
;; returning zero

  ;; (defn count-of-nucleotide-in-strand [nucleotide strand]
  ;;   (if (empty? strand)
  ;;     0
  ;;     (get (frequencies strand) nucleotide (throw (Throwable.)))))

  ;; (count-of-nucleotide-in-strand \T "GGGGGTAACCCGG")

;; using a function as the not-found
  ;; (get {:a 1 :b 2} :b (throw (Throwable.)))

;; unfortunately the throw expression is evaluated before the get
;; so we always get a Throwable exception even if the key is in the hash-map.
;; We can fix that with an or

  ;; (defn count-of-nucleotide-in-strand [nucleotide strand]
  ;;   (if (empty? strand)
  ;;     0
  ;;     (or (get (frequencies strand) nucleotide)
  ;;         (throw (Throwable.)))))
  ;;
  ;; (defn nucleotide-counts [strand]
  ;;   (frequencies strand))
  ;;
;; fails the empty case,
;; but we can add an empty result as the default value
;; and merge it with the resulting frequencies

;; (defn nucleotide-counts [strand]
  ;;   (merge {\A 0 \C 0 \G 0 \T 0} (frequencies strand)))
  ;;
