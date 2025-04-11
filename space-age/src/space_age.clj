(ns space-age)

(defn seconds-to-earth-years
  "Convert sections to earth years"
  [seconds]
  (/ seconds 31557600.0))

(defn relative-age
  "Return function to calculate age
  based on each planets relative orbit"
  [realtve-orbit]
  (fn [seconds]
    (let [earth-years (seconds-to-earth-years seconds)]
      (/ earth-years realtve-orbit))))


(def on-mercury (relative-age 0.240846))
(def on-venus   (relative-age 0.6151972))
(def on-earth   (relative-age 1))
(def on-mars    (relative-age 1.8808158))
(def on-jupiter (relative-age 11.862615))
(def on-saturn  (relative-age 29.447498))
(def on-neptune (relative-age 164.79132))
(def on-uranus  (relative-age 84.016846))
