(ns design-journal)

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;; Getting the individual digits in a number
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; A number is a single value in  Clojure,
  ;; a string is a collection of characters,
  ;; so the number could be converted to a string and iterated.

  ;; Once a string, it can be treated as a sequence of characters
  (count (str 153))

  #_(let [number-as-string (str 153)
          number-of-digits (count number-as-string)]
      (reduce (fn [digit]
                (* digit number-of-digits))
              0
              number-as-string))

  (let [number-as-string (str 153)
        number-of-digits (count number-as-string)]
    (reduce #(* (Integer/parseInt (str %2)) %1) number-of-digits number-as-string))

  ;; This is okay, but it does require a lot of converting between types
  ;; number > string > character > integer

  ;; Edn read string is a little more sophisticated

  (clojure.edn/read-string "153")
  ;; => 153

  ;; It doesnt work with characters
  #_(clojure.edn/read-string \1)


  ;; keeping numbers as numbers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Numbers are in base 10, so dividing by powers of 10 can separate a number

  (mod 153 10)
  ;; => 3
  (quot 153 10)
  ;; => 15
  (quot (quot 153 10) 10)
  ;; => 1

  ;; iterate over the number using quot on the result
  ;; left as a comment as iterate generates an infinite sequece
  #_(iterate #(quot % 10) 153)

  (take 3 (iterate #(quot % 10) 153))
  ;; => (153 15 1)

  ;; iterate continues to generate 0 numbers
  (take 4 (iterate #(quot % 10) 153))
  ;; => (153 15 1 0)

  ;; As we dont know how many digits,
  ;; use a conditional take
  ;; pos? checks for a number greater than zero.

  (take-while pos? (iterate #(quot % 10) 153))
  ;; => (153 15 1)

  (take-while pos? (iterate #(quot % 10) 12345))
  ;; => (12345 1234 123 12 1)

  ;; Then get the last digit of each number
  ;; by dividing by 10 and keeping the remainder

  (rem 12345 10)
  ;; => 5

  ;; Putting these three expressions together

  (map #(rem % 10)
       (take-while pos? (iterate #(quot % 10) 153)))

  ;; Now we have the individual numbers
  ;; raise each number to the power of the number of characters

  ;; Counting the numbers is simple now they are separated
  (count
    (map #(rem % 10)
         (take-while pos? (iterate #(quot % 10) 153))))

  ;; Clojure core does not have a function to raise a number to a power,
  ;; however, 5^3 is the same as (* 5 5 5)

  (repeat 3 5)
  ;; => (5 5 5)

  ;; calculate the total
  (defn raise-to-power
    [number power]
    (reduce * (repeat power number)))



  (defn armstrong?
    [number]
    (let [;; Individual digits from the number
          digits (map #(rem % 10)
                      (take-while pos? (iterate #(quot % 10) number)))

          ;; number of digits, used as the power
          power (count digits)]

      (= number
         (reduce + (map #(raise-to-power % power) digits)))))


  ;; or use the threading macro to show the transformation as a pipline


  (defn armstrong?
    [number]
    (let [;; Individual digits from the number
          digits (map #(rem % 10)
                      (take-while pos? (iterate #(quot % 10) number)))

          ;; number of digits, used as the power
          power (count digits)]

      (->> digits
           (map #(raise-to-power % power))
           (reduce +)
           (= number))))



  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Mentoring: reasonable solution

  (ns armstrong-numbers)

  (defn expt [num pow]
    (reduce * (repeat pow num)))

  (defn digits [n]
    (->> n
         (iterate #(quot % 10))
         (take-while pos?)
         (map #(rem % 10))))

  (defn armstrong? [num]
    (let [d    (digits num)
          dcnt (count d)]
      (->> d
           (map #(expt % dcnt))
           (reduce +)
           (= num))))


  ;; Common suggestions

  ;; Most users seem to split the number into digits by iterating a string and parsing characters. Suggest that this can be done arithmetically as well.
  ;; Often people use Math/pow which uses floating point arithmetics. Suggest using integer arithmetics and discuss the probems of comparing floating point values for equality.

  ;; Talking points
  ;; Mentoring notes

  ;; If the user comes to Clojure without experience in functional programming, this is the first exercise, that requires him to think non-imperative, and probably his first contact with sequences.


  ) ;; End of rich comment block





;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/mentor/solutions/ba58db6e68d04fe1a78560ec41dc1ea9


  (defn split-in-digits [n]
    (->> n
         str
         (map (comp read-string str))
         (reduce conj [])))

  (defn exponentiation [num exp]
    (loop [acc 1
           e   exp]
      (if (= e 0)
        acc
        (recur (* num acc) (dec e)))))

  (defn apply-exp [p coll]
    (let [limit (count coll)]
      (loop [result []
             cnt    0]
        (if (= cnt limit)
          result
          (recur (conj result (exponentiation (get coll cnt) p)) (inc cnt))))))

  (defn armstrong? [num]
    (let[coll  (split-in-digits num)
         power (count coll)]
      (->> coll
           (apply-exp power)
           (reduce +)
           (= num))))


  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/mentor/solutions/85eff4ea1855455badb99ac75c1221ab

  (def char-to-int #(Character/digit % 10))

  (defn get-digits-seq
    "Function returns seq of digits of given number."
    [num]
    (map char-to-int (seq (str num))))

  (defn pow-digits
    "Function returns sum of numbers in sequence of powered digits of given number."
    [num]
    (let [d (get-digits-seq num)]
      (apply +' (map #(.pow (biginteger %) (count d)) d))))

  (defn armstrong?
    [num]
    (= num (pow-digits num)))

  ) ;; End of rich comment block




;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/mentor/solutions/50248d79600b4ebf91b363e3165ab7e5

  (ns armstrong-numbers
    (:require [clojure.math.numeric-tower :refer [expt]]))

  (defn armstrong? [num]
    (let [digits (->>
                   num
                   (iterate #(quot % 10))
                   (take-while pos?)
                   (map #(rem % 10)))
          length (count digits)
          sum    (->>
                   digits
                   (map #(expt % length))
                   (reduce +))]
      (= num sum)))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns armstrong-numbers)

  (defn armstrong? [num]
    (let [num-str (str num)
          len     (count num-str)]
      (->> num-str
           (map #(Math/pow (Character/digit % 10) len))
           (reduce +)
           (== num))))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns armstrong-numbers)

  (defn armstrong? [num]
    (let [num-str (str num)
          len     (count num-str)]
      (->> num-str
           (map #(Math/pow (Character/digit % 10) len))
           (reduce +)
           (== num))))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns armstrong-numbers)

  (defn armstrong? [num]
    (let [s      (str num)
          length (count s)
          digit  #(- (int %) (int \0))
          exp    #(reduce * (repeat length %1))]
      (= num
         (reduce + (map (comp exp digit) s)))))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns armstrong-numbers)

  (defn digits [n]
    (->> n
         (iterate #(quot % 10))
         (take-while pos?)
         (mapv #(mod % 10))
         (rseq))
    )

  (defn armstrong? [n]
    (let [exp-length #(reduce * (repeat (count (digits n)) %))]
      (->> n
           (digits)
           (map exp-length)
           (reduce +)
           (= n))))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (defn armstrong? [num] ;; <- arglist goes here
    (== num
        (reduce + 0
                (map
                  #(reduce * (repeat (count (str num))
                                     (Character/digit % 10)))
                  (str num)))))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns armstrong-numbers)

  (defn armstrong? [num]
    (let [n  (count (str num))
          bi (biginteger num)]
      (= bi (reduce + (map #(.pow (biginteger (Character/digit % 10)) n) (str num))))))


  ) ;; End of rich comment block













;; (defn int->digits [n]
;;   (->> (iterate #(quot % 10) n)
;;        (take-while pos?)
;;        (mapv #(rem % 10))))

;; (defn armstrong? [n]
;;   (let [digits (int->digits n)
;;         exp    (count digits)]
;;     (->> (map #(pow % exp) digits)
;;          (reduce +)
;;          (= n))))


;; I am afraid I find your code harder to read, mainly because the names used have no meaning to me (naming is hard).

;; I find the `pow` name very cryptic and requires you to have a level of mathematical terminology not covered in the original challenge description.  The same goes for base and exponent.

;; When these terms are an acceptable requirement for accessing this code, then this is fine.  However, there is no context in the description or code that explains these terms.

;; raise to power is explicitly mentioned in the challenge description, so seems a far better way to express the code in relation to the information provided by the user.

;; In terms of extracting functionality into a helper function like `int->digits`, that is valuable when the code is likely to be used by other functions in the namespace.  Or the code is complex enough to warrant a separate function.  There seems no value in this case to do so, but in general it can be a useful approach.

;; I dont use `defn-' as it serves very little purpose in a language like Clojure.  The namespace is a far better approach to managing scope of functions.  The private scope of a `defn-` is easily side-tracked.

;; Using the `{^:private`} meta-data tag can have value where you explicitly do not wish others using your code to access that function, e.g. in an open source library.  However, I still prefer using a separate namespace where relevant.
