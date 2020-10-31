(ns design-journal)


;; Initial thought are to create a hash-map as a simple dictionary lookup.
;; Given a DNA nucleotide, e.g a character from the DNA string, find the key that is the same as the character and return the keys value.


(def dictionary-dna-rna
  "Convert DNA to RNA"
  {"G" "C"
   "C" "G"
   "T" "A"
   "A" "U"}
  )

;; Write a function to get the value for a given key (dna nucleotide)

(defn dna-nucleotide
  "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
  [dictionary nucleotide]
  (get dictionary (str nucleotide)))

;; simplify the function by using the hash-map as a function
;; provide a not-found value if the key is not in the map, so that the resulting RNA code can be checked for bad input data.

(defn convert-nucleotide
  "Convert a specific nucleotide from a DNA strand,
  into a nucleotide for an RNA strand"
  [dictionary nucleotide]
  (dictionary (str nucleotide) "X"))

;; To pass the tests the dna string can simply be checkef if it contains X.  Alhtough this satisfies the tests, its probably not meeting the full scope of the challenge.
;; If X is found in the dna string, then return an assertion error, using a string to make the error more informative.

#_(defn to-rna [dna]
    (if (clojure.string/includes? dna "X")
      (throw (AssertionError. "Unknown nucleotide"))
      (clojure.string/join
        (map #(convert-nucleotide dictionary-dna-rna %) dna))))


;; Restart in thinking
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; To convert a collection of values, define a hash-map where the keys are the initial value and the values are the transformed value (conversion, encoding, etc).  This is often refered to as a dictionary.

;; A string can be used as a collection of character values.

{\G \C \C \G \T \A \A \U}

;; Then map this dictionary over the dna sting (collection of characters) to create the RNA transcription.
;; Use an anonymous function to wrap the dictionary and pass each a character (nucleotide) from the DNA string in turn.

(defn to-rna
  [dna]
  (map (fn [nucleotide] (get {\G \C \C \G \T \A \A \U} nucleotide))
       dna))

(to-rna "GCTA")
;; => (\C \G \A \U)

;; > #(get {\G \C \C \G \T \A \A \U} %) is the same as using the dictionary hash-map as a function.

;; The result is returned as a sequence of characters.
;; Use `clojure.string/join` to return the RNA value as a string

(defn to-rna
  [dna]
  (clojure.string/join
    (map (fn [nucleotide] (get {\G \C \C \G \T \A \A \U} nucleotide))
         dna)))


(to-rna "GCTA")
;; => "CGAU"

;; What about when the nucleotide is invalid?
;; The hash-map can return a default value, which is nil if not explicitly defined

;; How about adding the throw as the not-found value?

(defn to-rna
  [dna]
  (clojure.string/join
    (map (fn [nucleotide ](get {\G \C \C \G \T \A \A \U} nucleotide
                               (throw (AssertionError. "Unknown nucleotide")) ))
         dna)))

;; Unfortunately this will evaluate the throw expression regardless of if the nucleotide is found in the hash-map, so always fails.

;; `or` function to the rescue
;; The `or` function will evaluate the first expression and if a true value is returned then any additional expressions are skipped over.
;; If the first expression returns false or a falsey value, i.e. `nil`, then the next expression is evaluated.

(defn to-rna
  [dna]
  (clojure.string/join
    (map (fn [nucleotide](or (get {\G \C \C \G \T \A \A \U} nucleotide)
                             (throw (AssertionError. "Unknown nucleotide"))))
         dna)))

(to-rna "GCTA")
;; => "CGAU"


(to-rna "GCXA")

;; an AssertionError is thrown as the `X` character does not exist in the dictionary hash-map, so the `get` expression returns `nil`.


;; Refactor and streamline

;; remove the get
;; A hash-map can be called as a function and takes a key as an argument.  This acts the same as the get function, returning the value  associated to a matching key, otherwise returning `nil` or the not-found value if specified.

(defn to-rna
  [dna]
  (clojure.string/join
    (map (fn [nucleotide ](or ({\G \C \C \G \T \A \A \U} nucleotide)
                              (throw (AssertionError. "Unknown nucleotide"))))
         dna )))


;; using the anonymous function syntax sugar
`#(* %1 %2)` is the same as `(fn [value1 value2] (+ value1 value2))`
;; the syntax sugar is often use with `map`, `reduce`, `apply` functions as the function definition tends to be compact and of single use.

;; If the function definition is more complex or used elsewhere in the namespace, then the `defn` function should be used to define shared behavior.


(defn to-rna
  [dna]
  (clojure.string/join
    (map #(or ({\G \C \C \G \T \A \A \U} %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))

;; remove the hard-coded hash-map

(def dictionary-dna-rna {\G \C \C \G \T \A \A \U})

(defn to-rna
  [dna]
  (clojure.string/join
    (map #(or (dictionary-dna-rna %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))



;; Making the function pure
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Its beyond the scope of the Exercism challenge, however, its recommended to use pure functions where possible.
;; A pure function only uses data from its arguments.
;; Adding a dictionary as an argument to the `to-rna` function would be simple-ident?
;; requires refactoring the unit tests
;; Add dna->rna to its own namespace and create parallel tests which use the 2 arity function

(defn to-rna
  [dictionary dna]
  (clojure.string/join
    (map #(or (dictionary %)
              (throw (AssertionError. "Unknown nucleotide")))
         dna )))

;; With a dictionary as an argument the function is also more usable, as other dictionaries could be used with the function.

;; The function would now be called as follows

(to-rna dictionary-dna-rna "GTGAC")



;; Other interesting points
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Convert a string into a sequence
;; This is a little superfluous as many clojure.core functions will treat a string as a collection of characters by default.

(into [] "ABCD")
;; => [\A \B \C \D]


;; Checks for nucleotides in the dictionary can be done with the `contains?` function.
;; This checks to see if the value is a key in the hash-map.
;; Contains does not check against the values in a map.  It also checks different things depending on the type of collection.

(contains? dictionary-dna-rna "X")
;; => false


;; The conversion could take place on the whole rna sequence and check if a correct sequence has been created
;; using the map to generate a placeholder value if passed an incorrect nucleotide.

(convert-nucleotide dictionary-dna-rna "B")



;; Using Contains to validate the dna sequence
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Check the dna sequence contains all valid characters
;; If so, then replace characters with their rna equivalent

;; Good approach if replacement of characters use considerable compute resource
;; compared to validating the dna sequence.
;; Otherwise the dna sequence is traversed twice, which may not be that efficient.

;; Test performance of this approach verses the recommended solution
;; especially on large dna sequences. https://github.com/hugoduncan/criterium

(def dna->rna {\G \C
               \C \G
               \T \A
               \A \U})

(defn valid-dna? [n]
  (contains? dna->rna n))

(defn to-rna [dna]
  (assert (every? valid-dna? dna))
  (clojure.string/join (replace dna->rna dna)))

(to-rna "GATTAGA")
