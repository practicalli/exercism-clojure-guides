(ns design-journal)

;; Talking points

;; Calling (two-fer "you") vs. returning the string directly.

(reduce #(str %2 %1) "" "hello")

(reduce conj '() "hello")

;; A more idiomatic and preferable approach for production code would be to use the clojure.string/join function.

;; Another approach is to solving this challenge is to use a custom function with reduce.

;; Define a function that creates a string of the two arguments it receives, swapping the order of the arguments. Use that function with reduce and two other arguments, the empty string, "" and the string to be reversed.

;; As reduce iterates over the string to be reversed, it builds up a new string in reverse order.

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Variable arguments approaches

  (defn two-fer-var-arg
    "Using variable arguments"
    [& name]
    (if (empty? name)
      "One for you, one for me."
      (str "One for " (first name) ", one for me.")))

  (two-fer-var-arg)
  (two-fer-var-arg "Ford")

  (defn two-fer-var-arg-apply
    "Using variable arguments and apply"
    [& name]
    (if (empty? name)
      "One for you, one for me."
      (str "One for " (apply str name) ", one for me.")))

  (two-fer-var-arg-apply)
  (two-fer-var-arg-apply "Ford")

  (defn two-fer-var-arg-apply-when-let
    "Using variable arguments and apply"
    [& name]
    (let [name (or (first name) "you")]
      (str "One for " name ", one for me.")))

  (two-fer-var-arg-apply-when-let)
  (two-fer-var-arg-apply-when-let "Arthur"))

   ;; End of rich comment block

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Alternative solutions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Using format and or

  (defn two-fer [& [name]]
    (format "One for %s, one for me."
            (or name "you"))))

   ;; End of rich comment block

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Seems a little complex for the challenge
  ;; partial, fnil and as->

  (defn two-fer [& [name]]
    (-> (partial format "One for %s, one for me.")
        (fnil "you")
        (as-> f (f name)))))

   ;; End of rich comment block
