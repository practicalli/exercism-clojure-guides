(ns design-journal)

;; ---------------------------------------------------------
(comment

  (require '[clojure.string :as string])
  ;; Frequencies is a very useful function
  ;; Given a string it will return how often the characters occur
  (frequencies "word")
  ;; => {\w 1, \o 1, \r 1, \d 1}

  ;; Our test data is of the form
  ;; "one fish two fish red fish blue fish"

  (frequencies "one fish two fish red fish blue fish")
  ;; => {\space 7, \b 1, \d 1, \e 3, \f 4, \h 4, \i 4, \l 1, \n 1, \o 2, \r 1, \s 4, \t 1, \u 1, \w 1}

  ;; Given a collection, eg. a vector of strings
  ;; frequencies will return how often each word in the collection appears
  (frequencies ["word"])
  ;; => {"word" 1}

  (frequencies ["one" "fish" "two" "fish" "red" "fish" "blue" "fish"])
  ;; => {"one" 1, "fish" 4, "two" 1, "red" 1, "blue" 1}

  ;; Using frequencies we can get the shape of data required,
  ;; however, the string needs to be split

  (string/split "one fish two fish red fish blue fish" #" ")

  (frequencies
   (string/split "one fish two fish red fish blue fish" #" "))
  ;; => {"one" 1, "fish" 4, "two" 1, "red" 1, "blue" 1}

  ;; This passes some of the tests,
  ;; however, when the string contains something other than words those tests fail.

  ;; (deftest ignore-punctuation
  ;;   (is (= {"car" 1, "carpet" 1 "as" 1 "java" 1 "javascript" 1}
  ;;          (word-count/word-count "car : carpet as java : javascript!!&@$%^&"))))

  ;; (deftest include-numbers
  ;;   (is (= {"testing" 2 "1" 1 "2" 1}
  ;;          (word-count/word-count "testing, 1, 2 testing"))))

  ;; (deftest normalize-case
  ;;   (is (= {"go" 3}
  ;;          (word-count/word-count "go Go GO"))))

;; Alternatively, use `re-seq` with a regular expression

  (re-seq #"\w+" "one fish two fish red fish blue fish")

  (frequencies
   (re-seq #"\w+" "one fish two fish red fish blue fish"))

  ;; Still caught by the last test, where the same word with different case
  ;; are counted as different words

  ;; (deftest normalize-case
  ;;   (is (= {"go" 3}
  ;;          (word-count/word-count "go Go GO"))))

  (frequencies
   (re-seq #"\w+" "go Go GO"))

  ;; Using lower-case then all the words would be counted as the same.

  (frequencies
   (map string/lower-case (re-seq #"\w+" "go Go GO"))))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
(comment

  (defn word-count [words]
    (->> words
         string/lower-case
         (re-seq #"\b\w+\b")
         frequencies)))
;; ---------------------------------------------------------
