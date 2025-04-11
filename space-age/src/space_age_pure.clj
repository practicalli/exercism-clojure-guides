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


#_(def relative-orbits
    "Orbital periods for planets in the sol system,
  relative to the orbit of Earth"
    {:earth   3.15576E7
     :jupiter 3.7435565912399995E8
     :mars    5.9354032690079994E7
     :mercury 7600543.81992
     :neptune 5.200418560032E9
     :saturn  9.292923628848001E8
     :uranus  2.6513700193296E9
     :venus   1.9414149052176E7 })

#_(defn age-on-planet
    [relative-orbits planet duration]
    (/ duration (planet relative-orbits)))



(eval
  `(do
     ~@(for [[planet mult] multipliers]
         `(defn ~(symbol (str "on-" (name planet)))
            ~(str "Returns age in years on " (name planet) " given age in seconds.") [~'a]
            (/ ~'a ~(* (+ 365 1/4) 24 60 60 mult))))))


;; Another evil macro version
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def earth-years {:mercury 0.2408467
                  :venus   0.61519726
                  :earth   1.0
                  :mars    1.8808158
                  :jupiter 11.862615
                  :saturn  29.447498
                  :uranus  84.016846
                  :neptune 164.79132})

(defn convert-fn-template [planet]
  `(def ~(symbol (str "on-" (name planet)))
     (fn [~'sec]
       (-> ~'sec
           (#(/ % 31557600)) ; sec -> earth years
           (#(/ % (earth-years ~planet))))))) ; earth years -> planet years

                                        ; generate conversion functions for each planet
(eval `(do ~@(map convert-fn-template (keys earth-years))))


;; Very nice higher order function
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn- seconds-to-earth-years [seconds]
  (/ seconds 31557600.0))

(defn- age-fn [year-ratio]
  (fn [seconds]
    (let [earth-years (seconds-to-earth-years seconds)]
      (/ earth-years year-ratio))))

(def on-mercury (age-fn 0.240846))
(def on-venus   (age-fn 0.6151972))
(def on-earth   (age-fn 1))
(def on-mars    (age-fn 1.8808158))
(def on-jupiter (age-fn 11.862615))
(def on-saturn  (age-fn 29.447498))
(def on-neptune (age-fn 164.79132))
(def on-uranus  (age-fn 84.016846))
