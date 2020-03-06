(ns hamming)



;; First thought is to use an anonymous function with filter

#_(defn distance [strand1 strand2] ; <- arglist goes here
    ;; your code goes here
    (filter #(= %1 %2) strand1 strand2)
    )

;; unfortunately filter only works over one data structure.

;; clojure.core/compare simple values and collections
;; https://clojuredocs.org/clojure.core/compare

#_(defn distance [strand1 strand2] ; <- arglist goes here
    ;; your code goes here
    (compare strand1 strand2))


;; Solves a few tests, but not exactly the solution we are looking for.
;; A very useful function to be aware of though.


;; If the values are not the same lenght, return nil
;; this can be done with a simple if expression.

#_(defn distance [strand1 strand2] ; <- arglist goes here
    ;; your code goes here
    (if (= (count strand1) (count strand2))
      (compare strand1 strand2)
      nil))


;; using map we can iterate over multiple functions
;; starting with just a simple compare condition

#_(map (fn [a b]
         (if (= a b)
           0
           1))
       "GGACTGA" "GGACTGA")

;; with different strands

#_(map (fn [a b]
         (if (= a b)
           0
           1))
       "GCACTGA" "GGACTGA")

;; now we can just add up the numbers to get the hammer value

#_(apply +
         (map (fn [a b]
                (if (= a b)
                  0
                  1))
              "GCACTGA" "GGACTGA"))


#_(defn distance [strand1 strand2]
    ;; your code goes here
    (if (= (count strand1) (count strand2))
      (apply +
             (map (fn [a b]
                    (if (= a b)
                      0
                      1))
                  strand1 strand2))
      nil))


;; refactor using the function definition syntax short form

#_(defn distance [strand1 strand2]
    ;; your code goes here
    (if (= (count strand1) (count strand2))
      (apply +
             (map #(if (= % %2) 0 1)
                  strand1 strand2))
      nil))

;; refactor to a when rather than if,
;; as only one branch required.

;; if returns nil if a second expression is not defined
;; and the condition is false
(if (= 1 2)
  "not equal")


(defn distance [strand1 strand2]
  ;; your code goes here
  (when (= (count strand1) (count strand2))
    (apply + (map #(if (= % %2) 0 1) strand1 strand2))))


;; a slightly more abstract approach
;; map not equals over the two strands

(map not= "ABC" "AOC")

;; filter the result with identity

(filter identity (map not= "ABC" "AOC"))

;; then count how many elements were returned
;; and that is the number of characters that are different

(count
  (filter identity (map not= "ABC" "AOC")))


(defn distance [strand1 strand2]
  (when (= (count strand1) (count strand2))
    (count (filter identity (map not= strand1 strand2)))))

;; not a huge difference in terms of size,
;; but feels more generic


;; Other uses for identity
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; flattening a nested structure
(mapcat identity [[[0 1] [1 2]] [[11 12]]])

;; replacement for `or` and `and`
;; they are both macros, so cannot be used as an argument function

;; and

#_(apply and [true 1 "yes"])

(every? identity [true 1 "yes"])

(every? identity [true nil "yes"])
