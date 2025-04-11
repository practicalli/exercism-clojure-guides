;; ---------------------------------------------------------
;; Exercisim: Gigasecond
;;
;; Given a moment, compute the moment that would be after an arbitrary number
;; of seconds has passed (one Gigasecond for this example).
;;
;; This function works is extreme cases like:
;; * Periods of any size (any positive number of seconds)
;; * Periods that include years like 1900 inside (oddly not leap year)
;; * Periods that start (or end), before (or after), the end of February in
;;   years that are (or not) leap years
;; * Periods that start (or end), before (or after), the start (or end) of any
;;   year
;; * Daylight savings time is not factored into the solution, as not all regions observer daylight savings
;; ---------------------------------------------------------


;; ---------------------------------------------------------
(ns design-journal)
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
(comment

  (ns design-journal
    [:import [java.time LocalDateTime]])

  (defn from [year month day]
    (let [birth-day       (LocalDateTime/of year month day 0 0 0)
          giga-second-day (.plusSeconds birth-day 1000000000)]
      [(.getYear giga-second-day)
       (.getMonthValue giga-second-day)
       (.getDayOfMonth giga-second-day)])))

   ;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Low level approach (overly complex)
(comment

;; Note: The number of seconds is embedded as `total-seconds` binding
  (defn from [y m d]
    ;; Steps:
    ;; 1) Convert time period to days
    ;; 2) Compute result thru 3 sections of the period (Start, Middle, End)
    ;;  2a) Start: Remaining days in first month of the period
    ;;  2b) Middle: Find last month of the period (loop through months/years)
    ;;  2c) End: Days in the last month of the period
    (letfn [;; Helper to compute boolean whether a year is leap or not
            (leap-year? [year]
              (cond
                (zero? (mod year 400)) true
                (zero? (mod year 100)) false
                :else                  (zero? (mod year 4))))

            ;; Helper to compute total number of days in a year's month
            (days-in-month [year month]
              (cond
                (= month 2)                    (if (leap-year? year) 29 28) ; February
                (some #(= month %) [4 6 9 11]) 30                           ; Abril, June, ...
                :else                          31)) ; January, March, ...

            ;; Helper to compute the number of remaining days of a date's month
            ;; Ex.: [2000 2 27] results in 3 days because 2000 is a leap year
            ;;      so day 27, day 28 and day 29 are 3 days in total.
            ;; (To obtain the total days of a particular month, set day to 1)
            (days-to-next-month [year month day]
              (+ (days-in-month year month) (- day) 1))]

      (let [total-seconds   1e9   ;; Gigasecond (works with any positive number)
            seconds-per-day 86400 ;; 24 x 60 x 60
            ;; Convert seconds to days
            total-days      (int (/ total-seconds seconds-per-day))]

        ;; Loop thru months decrementing the the total days remaining in period
        (loop [;; Initialize date and total days in period
               year      y
               month     m
               day       d
               remaining total-days]
          (let [;; Compute amount to jump according to distance to next month
                ;; First loop cycle computes from date's day (step 2a)
                ;; Following cycles compute from 1st of current month (step 2b)
                jump (days-to-next-month year month day)]
            ;; Out of the loop when no more room for another month
            (if-not (>= remaining jump)
              ;; Result (step 2c)
              [year month (+ day remaining)]
              ;; Update to next month and increment year when necesary
              (recur ;; Increment year when December jumps to January
               (if (zero? (mod month 12)) (inc year) year)
                ;; Circular month increment (December jumps to January)
               (inc (mod month 12))
               1 ;; 1st of month
                ;; Decrement the the total days remaining in period
               (- remaining jump)))))))))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Juxt tick library
(comment

  (ns design-journal
    (:require [tick.alpha.api :as t]))

  (defn from [y m d]
    (let [birth     (t/at (t/new-date y m d) "00:00")
          bill-secs (t/new-duration 1000000000N :seconds)
          final     (t/+ birth bill-secs)]
      [(t/int (t/year final))
       (t/int (t/month final))
       (t/day-of-month final)])))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Mostly "point-free" version using a Haskell-style flip:
(comment

  (defn- flip [f]
    #(apply f %2 % %&))

  (def from
    (comp (juxt (comp t/int t/year)
                (comp t/int t/month)
                t/day-of-month)
          (partial (flip t/+) (t/new-duration 1000000000N :seconds))
          (partial (flip t/at) "00:00")
          t/new-date)))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Java.tim
(comment

  (ns gigasecond
    (:import [java.time LocalDateTime]))

  (def gigasecond 1e9)

  (defn from [year month day]
    (as->
     (.plusSeconds (LocalDateTime/of year month day 0 0) gigasecond) date-time
      [(.getYear date-time) (.getMonthValue date-time) (.getDayOfMonth date-time)])))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Java Math library
(comment

  (ns gigasecond)

  (def days_in_gigasecond (Math/floor (/ 1000000000 60 60 24)))

  (def short_months [4 6 9 11])

  (defn leap [year]
    (and (zero? (mod year 4)) (or (not (zero? (mod year 100))) (zero? (mod year 400)))))

  (defn last-day-of-month [date]
    (or
     (and (= (date :month) 2)
          (if (leap (date :year))
            (= (date :day) 29)
            (= (date :day) 28)))
     (and (some #{(date :month)} short_months) (= (date :day) 30))
     (= (date :day) 31)))

  (defn add-day [date]
    (if (last-day-of-month date)
      (if (= (date :month) 12)
        {:year (inc (date :year)) :month 1 :day 1}
        {:year (date :year) :month (inc (date :month)) :day 1})
      {:year (date :year) :month (date :month) :day (inc (date :day))}))

  (defn from [year month day]
    (vals (reduce (fn [current, _] (add-day current)) {:year year :month month :day day} (range days_in_gigasecond)))))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Java.time
(comment

  (ns gigasecond
    (:import [java.time LocalDate]))

  (def days-of-gigasecond (long (/ 1000000000 86400)))

  (defn from [year month day]
    (let [giga-day (.plusDays (LocalDate/of year month day)
                              days-of-gigasecond)]
      [(.getYear       giga-day)
       (.getMonthValue giga-day)
       (.getDayOfMonth giga-day)])))

;; This code does not handle days of different length. (The length can change because of introduction/end of DST, because of leap seconds, or because of changes of the timezone of a country.)

  ;; But to take care of this we would have to know at least the locale and the time of birth. Without knowing this information, just adding a fixed number of days seems to be okay to me.

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Java Calendar
(comment

  (ns gigasecond)

;;; Java date manipulation helpers

  (defn- date-for
    "Get a Java Date for the given y/m/d."
    [y m d]
    (java.util.GregorianCalendar. y (dec m) d))

  (defn- date->ymd
    "Convert a Java Calendar to a y/m/d/ tuple."
    [date]
    [(.get date java.util.Calendar/YEAR)
     (inc (.get date java.util.Calendar/MONTH))
     (.get date java.util.Calendar/DAY_OF_MONTH)])

  (defn- seconds+
    "Add seconds to a Java date."
    [date seconds]
    (let [new-date (.clone date)]
      (.add new-date java.util.Calendar/SECOND seconds)
      new-date))

  (defn- gigasecond+
    "Add a gigasecond to the given date."
    [date]
    (seconds+ date 1000000000N))

  (defn from
    "Calculate the date one gigasecond from the given date.
  (Input and output dates are y/m/d tuples.)"
    [y m d]
    (-> (date-for y m d)
        gigasecond+
        date->ymd)))

   ;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Java.util.Date
(comment

  (ns gigasecond)

  (def ^:private gigamillisecond (* 1000000000 1000))

  (defn- javaDate->vector
    "Convert a java.util.Date into a vector of year month day"
    [^java.util.Date date]
    [(+ 1900 (.getYear date)) (inc (.getMonth date)) (.getDate date)])

  (defn- vector->javaDate
    "Convert a vector of year month day into a java.util.Date"
    ^java.util.Date [[y m d]]
    (java.util.Date. (- y 1900) (dec m) d))

  (defn from
    "Get the date of 1 gigasecond from given date"
    [y m d]
    (-> [y m d]
        (vector->javaDate)
        (.getTime)
        (+ gigamillisecond)
                                        ; cast milliseconds to a long so the java.util.Date.
                                        ; constructor can be resolved.
        (long)
        (java.util.Date.)
        (javaDate->vector))))

   ;; End of rich comment block
;; ---------------------------------------------------------

;; Alternate solutions
;; https://exercism.io/tracks/clojure/exercises/gigasecond/solutions/acccc3df02bf4d01b7f698e582d375b4
