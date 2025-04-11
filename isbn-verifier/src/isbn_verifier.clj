(ns isbn-verifier)

(defn- normalized-isbn [isbn]
  (->> isbn
       (re-find #"(\d)-?(\d{3})-?(\d{5})-?([\dX])$")
       next
       (apply str)))

(defn- parse-isbn-char [x]
  (if (= x \X)
    10
    (Character/digit x 10)))

(defn- isbn-sum [s]
  (->> s
       reverse
       (map-indexed (fn [idx itm] (* (inc idx) (parse-isbn-char itm))))
       (apply +)))

(defn isbn? [isbn]
  (let [s (normalized-isbn isbn)]
    (if (empty? s)
      false
      (zero? (mod (isbn-sum s) 11)))))
