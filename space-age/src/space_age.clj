(ns space-age)

;; orbital-period should be passed as an argument,
;; but that would break the exercism tests

(def orbital-period
  "Orbital periods for planets in the sol system"
  {:earth   365.25
   :jupiter 11.862615
   :mars    1.8808158
   :mercury 0.2408467
   :neptune 164.79132
   :saturn  29.447498
   :uranus  84.016846
   :venus   0.61519726})


;; Functions are a little verbose and not pure (constrained by Exercism tests)

(defn on-mercury [seconds]
  (/ seconds (* (:earth orbital-period) (:mercury orbital-period)) 24 60 60))

(defn on-venus [seconds]
  (/ seconds (* (:earth orbital-period) (:venus orbital-period)) 24 60 60))

(defn on-earth [seconds]
  (/ seconds (:earth orbital-period) 24 60 60))

(defn on-mars [seconds]
  (/ seconds (* (:earth orbital-period) (:mars orbital-period)) 24 60 60))

(defn on-jupiter [seconds]
  (/ seconds (* (:earth orbital-period) (:jupiter orbital-period)) 24 60 60))

(defn on-saturn [seconds]
  (/ seconds (* (:earth orbital-period) (:saturn orbital-period)) 24 60 60))

(defn on-neptune [seconds]
  (/ seconds (* (:earth orbital-period) (:neptune orbital-period)) 24 60 60))

(defn on-uranus [seconds]
  (/ seconds (* (:earth orbital-period) (:uranus orbital-period)) 24 60 60))
