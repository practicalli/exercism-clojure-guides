(ns armstrong-numbers)

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
