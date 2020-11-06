(ns space-age-pure)

;; A pure function approach to the solution
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Helpers
(def seconds->day
  (let [hours 24 minutes 60 seconds 60]
    (* hours minutes seconds)))


(def earth-relative-orbits
  "Orbital periods for planets in the sol system,
  relative to the orbit of Earth"
  {:earth   365.25
   :jupiter 11.862615
   :mars    1.8808158
   :mercury 0.2408467
   :neptune 164.79132
   :saturn  29.447498
   :uranus  84.016846
   :venus   0.61519726})

(defn age-on-planet
  [relative-orbits planet duration]
  (if (= planet :earth)
    (/ duration (:earth relative-orbits) seconds->day)
    (/ duration
       (* (:earth relative-orbits) (planet relative-orbits))
       seconds->day)))
