;; ---------------------------------------------------------
;; Exercisim: gigasecond
;;
;; See Design Journal namespace for many different examples
;; ---------------------------------------------------------


(ns gigasecond
  (:import [java.time LocalDate]))

(def days-of-gigasecond (long (/ 1000000000 86400)))

(defn from [year month day]
  (let [giga-day (.plusDays (LocalDate/of year month day)
                            days-of-gigasecond)]
    [(.getYear       giga-day)
     (.getMonthValue giga-day)
     (.getDayOfMonth giga-day)]))
