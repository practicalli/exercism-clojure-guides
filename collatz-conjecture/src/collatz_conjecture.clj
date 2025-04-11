(ns collatz-conjecture)

(defn collatz-step [num]
  (if (even? num)
    (/ num 2)
    (inc (* num 3))))

(defn collatz
  "Given a starting num, returns the count of steps needed for the Collatz Conjecture to reach 1"
  ([num]
   (assert (and (number? num)
                (> num 0))
           "num should be a positive number")
   (->> num
        (iterate collatz-step)
        (take-while #(not= % 1))
        count)))

(comment
  (collatz 20)) ;; 7
