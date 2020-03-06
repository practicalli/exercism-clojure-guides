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
