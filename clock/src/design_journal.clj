;; ---------------------------------------------------------
;; Heading summary title
;;
;; Brief description
;; ---------------------------------------------------------


;; ---------------------------------------------------------
(ns design-journal)
;; ---------------------------------------------------------

;; ---------------------------------------------------------
(defn clock->string [clock]
  (let [h (:hours clock)
        m (:minutes clock)]
    (format "%02d:%02d" h m)))

(defn minutes [clock]
  (+ (:minutes clock) (* (:hours clock) 60)))

(defn adjusted-minutes [n]
  (mod (+ n 1440) 1440))

(defn add-time [clock time]
  (let [m (adjusted-minutes (+ time (minutes clock)))]
    {:hours (quot m 60) :minutes (mod m 60)}))

(defn clock
  ([] {:hours 0 :minutes 0})
  ([hours minutes] (add-time (clock) (+ minutes (* hours 60)))))
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Mentor Feedback

;; This is a very clear solution and easy to understand.  I particularly like the use of the polymorphic `clock` function.

;; Using the let function helps break down the complexity of a function into smaller pieces.  However, it also introduces temporary local names.  It can also be challenging to think of meaningful local names and obscure names are a candidate for dropping the let or refactoring the code.

;; Sometimes it is more efficient to simply use the values inline, especially when those values are used only once.

;; It can be easier to use the let initially and then remove the let after the function is working if the let bindings are not required.

;; For example, the `clock->string` function could be written as follows without loosing any clarity

;; ```
;; (defn clock->string [clock]
;;   (format "%02d:%02d" (:hours clock) (:minutes clock)))
;; ```

;; The `add-time` function uses the let value in several places, so makes sense to keep it.
;; ---------------------------------------------------------
