(ns design-journal)

;; Given a string of digits, output all the contiguous substrings of length n in that string in the order that they appear.
;; For example, the string "49142" has the following 3-digit series:
;; "491"
;; "914"
;; "142"

;; And the following 4-digit series:
;; "4914"
;; "9142"

;; And if you ask for a 6-digit series from a 5-digit string, you deserve whatever you get.
;; Note that these series are only required to occupy adjacent positions in the input; the digits need not be numerically consecutive.


;; Deconstruct the problem
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Lets look at the project Euler challenge
;; https://projecteuler.net/problem=8

;; So we want to find adjacent number sequence
;; from a potentially large sequence

;; In the exercisim challenge our numbers are strings
;; so that is a little bit easier to divide up.


;; Test data
;; (slices "" 1)
;; (slices "123" 0)
;; (slices "123" 1000)
;; (slices "123" 3)
;; (slices "12345" 3)



;; REPL experiments
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; To divide up a sequence we can use partition
;; setting how many partitions of the data we want to make

(partition 5 (range 20))
;; => ((0 1 2 3 4) (5 6 7 8 9) (10 11 12 13 14) (15 16 17 18 19))

;; partition can loose data

(partition 6 (range 20))
;; => ((0 1 2 3 4 5) (6 7 8 9 10 11) (12 13 14 15 16 17))

;; so there is partition all
(partition-all 6 (range 20))
;; => ((0 1 2 3 4 5) (6 7 8 9 10 11) (12 13 14 15 16 17) (18 19))


;; We want to create a range of sequences though...
;; and we can still use partition
;; with a step
;; So far there is an implied step of the partition size


(partition 4 2 (range 20))
;; => ((0 1 2 3) (2 3 4 5) (4 5 6 7) (6 7 8 9) (8 9 10 11) (10 11 12 13) (12 13 14 15) (14 15 16 17) (16 17 18 19))

;; For our challenge we want all the sequences
(partition 1 1 (range 20))


(partition 3 1 (range 20))
;; => ((0 1 2) (1 2 3) (2 3 4) (3 4 5) (4 5 6) (5 6 7) (6 7 8) (7 8 9) (8 9 10) (9 10 11) (10 11 12) (11 12 13) (12 13 14) (13 14 15) (14 15 16) (15 16 17) (16 17 18) (17 18 19))

(partition 100 1 (range 20))
;; => ()

(partition-all 100 1 (range 20))
;; => ((0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (5 6 7 8 9 10 11 12 13 14 15 16 17 18 19) (6 7 8 9 10 11 12 13 14 15 16 17 18 19) (7 8 9 10 11 12 13 14 15 16 17 18 19) (8 9 10 11 12 13 14 15 16 17 18 19) (9 10 11 12 13 14 15 16 17 18 19) (10 11 12 13 14 15 16 17 18 19) (11 12 13 14 15 16 17 18 19) (12 13 14 15 16 17 18 19) (13 14 15 16 17 18 19) (14 15 16 17 18 19) (15 16 17 18 19) (16 17 18 19) (17 18 19) (18 19) (19))




;; Test data
;; (slices "123" 0)

(partition 0 1 "123")
;; => (() () ())

(= [] '())

(= [] (partition 0 1 "123"))
;; => false

(= [] (flatten (partition 0 1 "123")))

;; flatten usually feels a bit naughty
;; and its a bit of a sledgehammer

;; Tests 1 and 3 pass though
;; Test 3 is:
;; (slices "123" 1000)


;; Test 2
;; (slices "123" 3)

(partition 3 1 "123")
;; => ((\1 \2 \3))

(partition 3 3 "123")
;; => ((\1 \2 \3))


(flatten (partition 3 1 "123"))

(into []
      (partition 3 1 "123"))

(map str
     (partition 3 1 "123"))


(map #(apply str %)
     (partition 3 1 "123"))




;; Try this with the tests
(defn slices
  [string length]
  (map #(apply str %)
       (partition length 1 string)))




;; Passes all tests except the zero length
;; actually returning ("" "" "")


;; We could put an if expression
;; to check for a zero length

(defn slices
  [string length]
  (if (= 0 length)
    [""]
    (map #(apply str %1)
         (partition length 1 string))))


(defn slices
  [string length]
  (if (zero? length)
    [""]
    (map #(apply str %1)
         (partition length 1 string))))




;; These values are all the same
;; so if we can rule out duplicates
;; then maybe we have a winner

(map identity [:one :two :one])

(apply map identity '("" "" ""))


;; One way to find Clojure functions is to extend your vocabulary
;; Search for duplicate in the browser
;; check its opposites
;; different
;; distinct

(distinct)


(distinct
  (map #(apply str %)
       (partition 0 1 "123")))


;; failing different tests


(reduce conj '(""))

(reduce conj
        (distinct
          (map #(apply str %)
               (partition 0 1 "123"))))


(into []
      (distinct
        (map #(apply str %)
             (partition 0 1 "123"))))



(defn slices
  [string length]
  (into []
        (distinct
          (map #(apply str %)
               (partition length 1 string))) ))


;; Answers summary
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (defn slices
    [string length]
    (into []
          (distinct
            (map #(apply str %)
                 (partition length 1 string))) ))



  (defn slices [string length]
    (->> string
         (partition length 1)
         (map #(apply str %))
         (distinct)
         (into [])))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment



  (defn slices
    [string length]
    (into []
          (distinct
            (map #(apply str %)
                 (partition length 1 string)))))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  (defn slices [string length]
    (->>
      (partition length 1 string)
      (map (partial apply str)) distinct))

  ) ;; End of rich comment block




#_(defn slices [string length]
    (if (zero? length)
      [""]
      (map string/join (partition length 1 string))))

(defn slices [string length]
  (distinct
    (map (partial apply str)
         (partition length 1 string))))


#_(defn slices [string length]
    (->>
      (partition length 1 string)
      (map (partial apply str))
      distinct))

(slices "123" 0)


(defn slices [string length]
  (distinct
    (map (partial apply str)
         (partition length 1 string))))

(defn slices [string length]
  (distinct
    (map #(apply str %)
         (partition length 1 string))))
