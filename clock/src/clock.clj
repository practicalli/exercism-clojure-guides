(ns clock)

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
