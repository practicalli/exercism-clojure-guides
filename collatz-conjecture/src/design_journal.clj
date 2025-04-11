;; ---------------------------------------------------------
;; Exercisim: Collatz Conjecture
;;
;; Design journal showing evolution of solution
;; ---------------------------------------------------------


;; ---------------------------------------------------------
(ns design-journal)
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Using a recursive function
  ;; with larger values this could blow up the stack

  (ns collatz-conjecture)

  (defn collatz [num]
    (cond
      (= 1 num)   0
      (even? num) (+ 1 (collatz (/ num 2)))
      :else       (+ 1 (collatz (+ (* 3 num) 1)))))

  (collatz 1))

;; refine the above using iterate and take-while
  ;; implement it with functions like iterate and take-while as an alternative
  ;; num)
  ;;               (iterate
  ;;                 #(if(even? %)
  ;;                    (/ % 2)
  ;;                    (+(* % 3) 1)) num)))

  ;; (collatz-improved 12)

  ;; I'm not getting how i can pass the current number of the iteration to the predicate function of the take-while.

;; You can do something like:

  ;; (->> num
  ;;      (iterate the-fn)
  ;;      (take-while bigger....)
  ;;      ....)

  ;; The function the-fn will implement the if etc...

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; comments:

  ;; To check for a condition you can use assert. No need to use if.
  ;; I would avoid using variable names that shadow functions like count.
  ;; Well done using recur to call the function recursively.
  ;; Having said that is always a good idea to check for existing functions that provide the desired functionality.
  ;; Please take a look at iterate and take-while.
  ;; You could extract the if to check if the number is even? to an auxiliary function.

  (defn collatz
    "Given a starting num, returns the count of steps needed for the Collatz Conjecture to reach 1"
    ([num]
     (if (or (not (number? num))
             (< num 1))
       (throw (AssertionError. "num should be a positive number"))
       (collatz 0)))
    ([num count]
     (if (= num 1)
       count
       (recur (if (even? num)
                (/ num 2)
                (inc (* num 3)))
              (inc count)))))

;; Refactor

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
          count))))
   ;; End of rich comment block
;; ---------------------------------------------------------
