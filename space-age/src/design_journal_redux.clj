(ns design-journal-redux)

;; Namespace api from the tests

(def on-mercury nil)
(def on-venus   nil)
(def on-earth   nil)
(def on-mars    nil)
(def on-jupiter nil)
(def on-saturn  nil)
(def on-neptune nil)
(def on-uranus  nil)

;; ---------------------------------------------------------
;; REPL experiments

(comment
  ;; Start with just Earth

  ;; Calculate earths orbital period in seconds
  ;; Calculate the hours, minutes and seconds

  (def orbital-period
    {:earth 365.25})

  ;; hours-in-a-year
  (* (:earth orbital-period) 24)
  ;; => 8766.0

  ;; minutes in a year
  (* (:earth orbital-period) 24 60)
  ;; => 525960.0

  ;; seconds in a year
  (* (:earth orbital-period) 24 60 60)
  ;; => 3.15576E7

  (defn seconds->years
    [seconds]
    (/ seconds (:earth orbital-period) 24 60 60))

  ;; The other planets

  ;; All other planet orbits are relative to that of earth,
  ;; so multiplying the Earth orbit by another planets orbit period
  ;; should give the correct answer

  ;; Create a data structure to hold the relative planet orbits

  (def orbital-period
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

  ;; Then use the seconds->years function definition,
  ;; but multiply the relative orbit with the Earth orbit
  ;; So the on-mercury function definition would be:

  (defn on-mercury [seconds]
    (/ seconds (* (:earth orbital-period) (:mercury orbital-period)) 24 60 60))

  (on-mercury 2134835688)
  ;; => 280.87933423985845

  ;; Define a relative age function

  (defn relative-age
    [realtve-orbit]
    (fn [seconds]
      (let [earth-years (seconds-to-earth-years seconds)]
        (/ earth-years realtve-orbit))))

  (defn seconds-to-earth-years
    "Convert sections to earth years"
    [seconds]
    (/ seconds 31557600.0)))

;; ---------------------------------------------------------
;; Solutions

;; (defn seconds-to-earth-years
;;   "Convert sections to earth years"
;;   [seconds]
;;   (/ seconds 31557600.0))
;;
;; (defn relative-age
;;   "Return function to calculate age
;;   based on each planets relative orbit"
;;   [realtve-orbit]
;;   (fn [seconds]
;;     (let [earth-years (seconds-to-earth-years seconds)]
;;       (/ earth-years realtve-orbit))))
;;
;;
;; (def on-mercury (relative-age 0.240846))
;; (def on-venus   (relative-age 0.6151972))
;; (def on-earth   (relative-age 1))
;; (def on-mars    (relative-age 1.8808158))
;; (def on-jupiter (relative-age 11.862615))
;; (def on-saturn  (relative-age 29.447498))
;; (def on-neptune (relative-age 164.79132))
;; (def on-uranus  (relative-age 84.016846))

;; ---------------------------------------------------------
;; Alternative REPL experiments

#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; How to represent the data

  ;; Eath orbit is the number of days
  (def earth-orbit 365.25)

;; Planet orbits are relative to the earth

  (def relative-orbital-period
    "Orbital periods for planets in the sol system,
  relative to the orbit of Earth"
    {:jupiter 11.862615
     :mars    1.8808158
     :mercury 0.2408467
     :neptune 164.79132
     :saturn  29.447498
     :uranus  84.016846
     :venus   0.61519726})

;; Then use the seconds->years function definition,
  ;; but multiply the relative orbit with the Earth orbit
  ;; So the on-mercury function definition would be:

  (defn on-mercury [seconds]
    (/ seconds
       (* earth-orbit
          (:mercury relative-orbital-period))
       24 60 60))

  (on-mercury 2134835688)
  ;; => 280.87933423985845

;; Refactor
  ;; Reduce repeated calculations code

  ;; Transform the data

  ;; * Earth-orbital-period
  ;;   relative-planet-orbital-period
  ;;   hours minutes seconds

  (defn relative-orbit->orbit-duration
    "Calculate a planets orbital duration,
    given its relative orbit to the earth"
    [relative-orbit]
    (let [earth-orbital-period 365.25]
      (* earth-orbital-period
         relative-orbit
         24 60 60)))

  (relative-orbit->orbit-duration 0.2408467)
  ;; 7600543.81992

;; On-mercury test seconds: 2134835688
  (/ 2134835688  7600543.81992)

;; Define a function to iterate over all of the relative orbits

  ;; Map: relative-orbital-period
  ;; Function: relative-orbit->orbit-duration

  (defn update-orbital-period
    [m f]
    (reduce-kv
     (fn [m k v] (assoc m k (f v)))
     {}
     m))

  (update-orbital-period
   relative-orbital-period
   relative-orbit->orbit-duration)

  ;; Define a transformed map

  (def planet-orbit-duration
    (update-orbital-period
     relative-orbital-period
     relative-orbit->orbit-duration))

  planet-orbit-duration
;;{:jupiter 3.7435565912399995E8,
;; :mars 5.9354032690079994E7,
;; :mercury 7600543.81992,
;; :neptune 5.200418560032E9,
;; :saturn 9.292923628848001E8,
;; :uranus 2.6513700193296E9,
;; :venus 1.9414149052176E7}

;; Add Earth duration
  (def planet-orbit-duration
    (merge
     (update-orbital-period
      relative-orbital-period
      relative-orbit->orbit-duration)
     {:earth (* earth-orbit 24 60 60)}))

  planet-orbit-duration
;;{:jupiter 3.7435565912399995E8,
;; :mars 5.9354032690079994E7,
;; :mercury 7600543.81992,
;; :neptune 5.200418560032E9,
;; :saturn 9.292923628848001E8,
;; :uranus 2.6513700193296E9,
;; :venus 1.9414149052176E7,
;; :earth 3.15576E7}

  #_()) ; End of rich comment

;; End of REPL experiments
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Alternative solutions

;; (def earth-orbit 365.25)
;;
;; (def relative-orbital-period
;;   "Orbital periods for planets in the sol system,
;;   relative to the orbit of Earth"
;;   {:jupiter 11.862615
;;    :mars    1.8808158
;;    :mercury 0.2408467
;;    :neptune 164.79132
;;    :saturn  29.447498
;;    :uranus  84.016846
;;    :venus   0.61519726})
;;
;; (defn relative-orbit->orbit-duration
;;     "Calculate a planets orbital duration,
;;     given its relative orbit to the earth"
;;     [relative-orbit]
;;     (let [earth-orbital-period 365.25]
;;       (* earth-orbital-period
;;          relative-orbit
;;          24 60 60)))
;;
;; (defn update-orbital-period
;;     [m f]
;;    (reduce-kv
;;      (fn [m k v] (assoc m k (f v)))
;;      {}
;;      m))
;;
;; (def planet-orbit-duration
;;     (merge
;;      (update-orbital-period
;;       relative-orbital-period
;;       relative-orbit->orbit-duration)
;;      {:earth (* earth-orbit 24 60 60)}))

;; End of Alternative solutions
;; ---------------------------------------------------------
