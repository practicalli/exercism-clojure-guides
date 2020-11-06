(ns space-age-pure-test
  (:require [space-age-pure :as sut]
            [clojure.test :refer [deftest is testing]]))


(defn- rounds-to
  [expected actual]
  (is (= (Math/round (* 100.0 expected))
         (Math/round (* 100.0 actual)))))

(deftest age-on-planet-test
  (testing "Planet Earth"
    (rounds-to 31.69 (sut/age-on-planet
                       sut/earth-relative-orbits
                       :earth
                       1000000000)))

  (testing "Planet Mercury"
    (let [seconds 2134835688]
      (rounds-to 67.65
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 280.88
                 (sut/age-on-planet sut/earth-relative-orbits :mercury seconds))))

  (testing "Planet Venus"
    (let [seconds 189839836]
      (rounds-to 6.02
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 9.78
                 (sut/age-on-planet sut/earth-relative-orbits :venus seconds))))

  (testing "Planet Venus"
    (let [seconds 2329871239]
      (rounds-to 73.83
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 39.25
                 (sut/age-on-planet sut/earth-relative-orbits :mars seconds))))

  (testing "Planet Jupiter"
    (let [seconds 901876382]
      (rounds-to 28.58
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 2.41
                 (sut/age-on-planet sut/earth-relative-orbits :jupiter seconds))))

  (testing "Planet Saturn"
    (let [seconds 3000000000]
      (rounds-to 95.06
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 3.23
                 (sut/age-on-planet sut/earth-relative-orbits :saturn seconds))))

  (testing "Planet Uranus"
    (let [seconds 3210123456]
      (rounds-to 101.72
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 1.21
                 (sut/age-on-planet sut/earth-relative-orbits :uranus seconds))))

  (testing "Planet Neptune"
    (let [seconds 8210123456]
      (rounds-to 260.16
                 (sut/age-on-planet sut/earth-relative-orbits :earth seconds))
      (rounds-to 1.58
                 (sut/age-on-planet sut/earth-relative-orbits :neptune seconds)))))
