(ns two-fer)

(defn two-fer
  ([] (two-fer "you"))
  ([name]
   (str "One for " name ", one for me.")))

(two-fer)
;; (two-fer "Zaphod")

;; (defn two-fer-var-arg
;;   "Using variable arguments"
;;   [& name]
;;   (if (empty? name)
;;     "One for you, one for me."
;;     (str "One for " (first name) ", one for me.")) )

;; (two-fer-var-arg)
;; (two-fer-var-arg "Ford")

;; (defn two-fer-var-arg-apply
;;   "Using variable arguments and apply"
;;   [& name]
;;   (if (empty? name)
;;     "One for you, one for me."
;;     (str "One for " (apply str name) ", one for me.")) )

;; (two-fer-var-arg-apply)
;; (two-fer-var-arg-apply "Ford")

;; (defn two-fer-var-arg-apply-when-let
;;   "Using variable arguments and apply"
;;   [& name]
;;   (let [name (or (first name) "you")]
;;     (str "One for " name ", one for me.")))

;; (two-fer-var-arg-apply-when-let)
;; (two-fer-var-arg-apply-when-let "Arthur")

;; Reasonable solutions

(ns two-fer)

(defn two-fer
  ([] (two-fer "you"))
  ([name] (str "One for " name ", one for me.")))

(defn two-fer [& [name]]
  (-> (partial format "One for %s, one for me.")
      (fnil "you")
      (as-> f (f name))))

;; Common suggestions
;; Talking points

;; Calling (two-fer "you") vs. returning the string directly.

(reduce #(str %2 %1) "" "hello")

(reduce conj '() "hello")

;; A more idiomatic and preferable approach for production code would be to use the clojure.string/join function.

;; Another approach is to solving this challenge is to use a custom function with reduce.

;; Define a function that creates a string of the two arguments it receives, swapping the order of the arguments. Use that function with reduce and two other arguments, the empty string, "" and the string to be reversed.

;; As reduce iterates over the string to be reversed, it builds up a new string in reverse order.
