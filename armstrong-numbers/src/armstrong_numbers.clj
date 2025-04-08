(ns armstrong-numbers)

;; Using floating point numbers leads to madness


;; Definitions
;; expt returns base-number raised to the power power-number.
;; https://en.wikipedia.org/wiki/Exponential_function

(ns armstrong-numbers)

(defn expt [base pow]
  (reduce * 1 (repeat pow base)))

(defn armstrong? [n]
  (let [digits (map (comp read-string str) (str n))
        l      (count digits)]
    (= n (reduce + (map #(expt % l) digits)))))




(defn raise-to-power
  [number power]
  (reduce * (repeat power number)))

(defn armstrong?
  [number]
  (let [;; Individual digits from the number
        digits (map #(rem % 10)
                    (take-while pos? (iterate #(quot % 10) number)))

        ;; number of digits, used as the power
        power (count digits)]

    (->> digits
         (map #(raise-to-power % power))
         (reduce +)
         (= number))))






(defn digits [n]
  (->> n
       (iterate #(quot % 10))
       (take-while pos?)
       (mapv #(mod % 10))
       (rseq))
  )

(defn armstrong? [n]
  (let [exp-length #(reduce * (repeat (count (digits n)) %))]
    (->> n
         (digits)
         (map exp-length)
         (reduce +)
         (= n))))

(armstrong? 153)


(mod 15 10)
(rem 15 10)



(defn armstrong? [num]
  (let [exp (fn [e n] (reduce * (repeat e n)))]
    (= num (loop [n      num
                  digits [0]
                  e      0]
             (if (>= n 1)
               (recur (quot n 10)
                      (conj digits (rem n 10))
                      (inc e))
               (->> digits
                    (map (partial exp (max e 1)))
                    (reduce +)))))))

(armstrong? 153)
