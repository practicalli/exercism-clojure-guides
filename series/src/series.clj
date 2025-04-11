(ns series)

#_(defn slices
    [string length]
    (distinct
     (map #(apply str %)
          (partition length 1 string))))

(defn slices [string length]
  (distinct
   (map (partial apply ,,, str)
        (partition length 1 string))))
