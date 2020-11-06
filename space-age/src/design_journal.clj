(ns design-journal)


;; Given an age in seconds, calculate how old someone would be on:

;; Earth: orbital period 365.25 Earth days, or 31557600 seconds
;; Mercury: orbital period 0.2408467 Earth years
;; Venus: orbital period 0.61519726 Earth years
;; Mars: orbital period 1.8808158 Earth years
;; Jupiter: orbital period 11.862615 Earth years
;; Saturn: orbital period 29.447498 Earth years
;; Uranus: orbital period 84.016846 Earth years
;; Neptune: orbital period 164.79132 Earth years

;; So if you were told someone were 1,000,000,000 seconds old, you should be able to say that they're 31.69 Earth-years old.

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;; Start with just Earth

  ;; Calculate earths orbital period in seconds
  ;; Calculate the hours, minutes and seconds

  (def orbital-period
    {:earth 365.25
     })


  ;; hours-in-a-year
  (* (:earth orbital-period) 24 )
  ;; => 8766.0

  ;; minutes in a year
  (* (:earth orbital-period) 24 60)
  ;; => 525960.0

  ;; seconds in a year
  (* (:earth orbital-period) 24 60 60)
  ;; => 3.15576E7

  (type
    (* (:earth orbital-period) 24 60 60))

  ;; Define a partial function to calculate the seconds in a year
  (def year-to-seconds (partial * 24 60 60))

  (year-to-seconds (:earth orbital-period))

  ;; or define a function
  (defn year->seconds
    [orbital-year]
    (* orbital-year 24 60 60))

  (->seconds 365.25)
  ;; => 3.15576E7


  ;; Seconds to years
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; The tests give a time in seconds
  ;; and the result should be years those seconds represet,
  ;; on a specific planet.

  (defn seconds->years
    [seconds]
    (/ seconds (:earth orbital-period) 24 60 60))

  (seconds->years 1000000000)
  ;; => 31.68808781402895

  ;; Format the return value to 2 decimal places
  ;; although  it returns the value as a string instead of a number

  (format "%.2f" (seconds->years 1000000000))
  ;; => "31.69"

  ;; Looking at the test code
  ;; it uses Java interoperability and calls the Math/round function in Java


  (= (Math/round (* 100.0 (seconds->years 1000000000)))
     (Math/round (* 100.0 31.69)))
  ;; => true

  ;; Multiplying the expected and actual numbers by 100, then rounding them,
  ;; effectively compares the numbers to 2 decimal places,
  ;; although they numbers are actually compared as integers

  ;; Math/round docs
  ;; Returns the closest int to the argument, with ties rounding to positive infinity


  ;; Multiplying by a decimal number will eagerly evaluate the number if it is a ratio type
  (* 100.0 22/7)

  ;; Although multiplying by an integer number will keep the ratio type
  (* 100 22/7)

  ;; So there is no need for rounding the value as the tests will do that for us.

  (defn seconds->years
    [seconds]
    (/ seconds (:earth orbital-period) 24 60 60))


  ;; The other planets
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

  ;; The same algorithm works for all other planets,
  ;; simply substitute the name of the planet


  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Refactor the design to use pure functions
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn on-mercury [orbits planet seconds]
    (/ seconds (* (orbits :earth) (orbits planet)) 24 60 60))

  (on-mercury orbital-period :mercury 2134835688)
  ;; => 280.87933423985845

  ;; This is pure but still a lot of repetition in each function
  ;; As the planet is passed as an argument,
  ;; A general funciton definition can be created

  (defn ->years [orbits planet seconds]
    (/ seconds (* (orbits :earth) (orbits planet)) 24 60 60))



  ;; Refactor the orbital periods to just be a single value
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Include the calculation of seconds into years in the hash-map
  ;; :jupiter (* (* 365.25 11.862615) 24 60 60)

  ;; The calculation can be flattened
  (= (* (* 365.25 11.862615) 24 60 60)
     (*  365.25 11.862615 24 60 60))

  (def relative-orbits
    "Orbital periods for planets in the sol system,
  relative to the orbit of Earth"
    {:earth   (* 365.25 24 60 60)
     :jupiter (* 365.25 11.862615 24 60 60)
     :mars    (* 365.25 1.8808158 24 60 60)
     :mercury (* 365.25 0.2408467 24 60 60)
     :neptune (* 365.25 164.79132 24 60 60)
     :saturn  (* 365.25 29.447498 24 60 60)
     :uranus  (* 365.25 84.016846 24 60 60)
     :venus   (* 365.25 0.61519726 24 60 60) })

  ;; This simplifies the functions for each planet

  (defn on-mercury [seconds]
    (/ seconds (:mercury relative-orbits)))

  (on-mercury 2134835688)
  ;; => 280.87933423985845


  ;; using a pure function and passing all the arguments

  (defn ->years
    [relative-orbits planet seconds]
    (/ seconds (planet relative-orbits)))

  ;; This is now a generic function, so all the separate functions are no longer needed.


  ;; Refactor the orbital periods to clarify numbers
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (def relative-orbits
    "Orbital periods for planets in the sol system,
  relative to the orbit of Earth"
    (let [earth-orbit      365.25
          seconds-in-a-day (* 24 60 60)]

      {:earth   (* earth-orbit seconds-in-a-day)
       :jupiter (* earth-orbit seconds-in-a-day 11.862615)
       :mars    (* earth-orbit seconds-in-a-day 1.8808158)
       :mercury (* earth-orbit seconds-in-a-day 0.2408467)
       :neptune (* earth-orbit seconds-in-a-day 164.79132)
       :saturn  (* earth-orbit seconds-in-a-day 29.447498)
       :uranus  (* earth-orbit seconds-in-a-day 84.016846)
       :venus   (* earth-orbit seconds-in-a-day 0.61519726) }))

  ;; There still seems to be a lot of redundancy here


  ;; Define a helper function
  (def seconds->day (* 24 60 60))

  ;; To make the code self-describing
  (def seconds->day
    (let [hours 24 minutes 60 seconds 60]
      (* hours minutes seconds)))


  ;; Simplify the hash-map of relative orbits

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

  (defn seconds->years
    [relative-orbits planet duration]
    (/ duration
       (* (:earth relative-orbits) (planet relative-orbits))
       seconds->day))


  (seconds->years earth-relative-orbits :mercury 2134835688)
  ;; => 280.87933423985845


  ;; There is one case that fails with the function,
  ;; when the planet is Earth

  (seconds->years earth-relative-orbits :earth 1000000000)


  ;; Add a condition to check for Earth
  ;; use a more meaningful name


  (defn age-on-planet
    [relative-orbits planet duration]
    (if (= planet :earth)
      (/ duration (:earth relative-orbits) seconds->day)
      (/ duration
         (* (:earth relative-orbits) (planet relative-orbits))
         seconds->day)))


  ;; Update the tests for the pure function
  ;; Use the existing `round-to` function

  (defn- rounds-to
    [expected actual]
    (is (= (Math/round (* 100.0 expected))
           (Math/round (* 100.0 actual)))))

  (deftest age-on-planet-test
    (testing "Planet Earth"
      (rounds-to 31.69 (sut/age-on-planet
                         sut/earth-relative-orbits
                         :earth
                         1000000000))))


  ;; Refactor all the other tests,
  ;; adding them as testing sections within the age-on-planet-test

  ) ;; End of rich comment block
