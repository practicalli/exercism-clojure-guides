(ns design-journal
  (:require [clojure.string]))


;; General design approach
;; define the set of rules that process the text as let bindings.  So you could have `question?`, `yelling?`, etc as the names bound to the expression that defines the text parsing rule.

;; Each name would have a value of true or false.

;; Then the `cond` section is simply one of those names, or a combination of names using `and` or `or`,  If a name in the `cond` is true, then it returns its associated phrase.

;; Also take a look at `re-seq`, `re-find` and `re-matches` in combination with a regular expression pattern.

;; I found this web page helpful for understanding regex patterns
;; [https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions/Cheatsheet](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions/Cheatsheet)



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Asking Bob a question?
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Search the tests for the string "Sure"
  ;; - string ends with ?

  ;; Is the last character in the string a ?
  (= \? (last "this is a question?"))

  (= \? (last "this is still a question ?"))

  (= \? (last "this is still a question? "))

  ;; trailing whitespace should be dropped
  ;; there is probably something in clojure.string for that

  (clojure.string/trimr "this is still a question? ")

  (= \? (last (clojure.string/trimr "this is still a question? ")))


  ;; Yelling at Bob
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Search the tests for the string "Whoa, chill out!"
  ;; - strings are all in Capital letters.

  ;; Review clojure.string API
  ;; - no function to check if a string is in capital letters
  ;; easy enough to write a comparison

  ;; convert the string to uppercase
  (clojure.string/upper-case "watch out!")

  ;; compare the uppercase version of the string with the original,
  ;; if they are equal, then the original string must have been in upper case

  (= "WATCH OUT!"
     (clojure.string/upper-case "WATCH OUT!"))

  (= "watch out!"
     (clojure.string/upper-case "watch out!"))

  ;; There is a flaw in this approach thought

  (= "1, 2, 3"
     (clojure.string/upper-case "1, 2, 3"))

  ;; Refined rule
  ;; - there are uppercase characters
  ;; - there are no lower case characters

  (defn shouting?
    [phrase]
    (not (re-find #"[a-z]" phrase)))

  (shouting? "HELLO")
  (shouting? "Hello")

  (shouting? "HELLO 1 2 3")
  ;; re-seq not going to work

  ;; #"\p{Ll}" matches any lower case unicode character
  ;; https://www.regular-expressions.info/unicode.html#category

  (defn shouting?
    [phrase]
    (not (re-find #"\p{Ll}" phrase)))


  ;; responds-to-no-letters test gives a false positive

  (shouting? "1 2 3")

  ;; Add a re-find to include numbers as an acceptable pattern to be present

  (defn shouting?
    [phrase]
    (and (re-find #"[A-Z]" phrase)
         (not (re-find #"[a-z]" phrase))))

  (shouting? "1 2 3")

  (shouting? "HELLO 1 2 3")


  ;; Yelling a question at Bob
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Search tests for the string  "Calm down, I know what I'm doing!"
  ;; - uppercase string that ends with a question mark

  ;; Code has already been created that tests both these aspects

  (defn yelling-question
    [phrase]
    (and (= \? (last (clojure.string/trimr phrase)))
         (and (re-find #"[A-Z]" phrase)
              (not (re-find #"[a-z]" phrase)))))

  (yelling-question "WHAT THE HELL WERE YOU THINKING?")
  (yelling-question "What the hell were you thinking?")


  ;; Address Bob but saying nothing
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Search tests for the string "Fine. Be that way!"
  ;; - empty string, whitespace or not an alphabetic character
  ;; - use a regex of alphabetic characters to filter out the string, is there anything left.  Need to also filter out ? too


  ;; Does the string contain special-characters
  ;; - use a regular expression to check for alphabetic characters
  (re-seq #"[a-zA-Z]" "hello")

  ;; use the ^ character with the alphabetic characters to invert the results,
  ;; so everything that is not an alphabetic character

  (re-seq #"[^a-zA-Z]" "hello")

  (re-seq #"[^a-zA-Z]" "\t\t\t\t")

  ;; We could just count the characters in the string
  (count (re-seq #"[^a-zA-Z]" "\t\t\t\t"))

  ;; and see if its bigger than zero, indicating that the question to bob is gibberish
  (< 0 (count (re-seq #"[^a-zA-Z]" "\t\t\t\t")))

  ;; using `seq?` is the idiomatic way to check if a sequence is empty
  (seq? (re-seq #"[^a-zA-Z]" "\t\t\t\t"))

  (seq? (re-seq #"[^a-zA-Z]" "\n\r \t"))


  ;; Does the string contain whitespace
  ;; rather than use a regular expression, use clojure.string functions
  (clojure.string/blank? "")
  (clojure.string/blank? "  ")

  (clojure.string/blank? "hello")
  ;; => false

  ;; Use both conditions together,
  ;; in this case an or as only one or the other needs to be true

  (or (clojure.string/blank? "")
      (seq? (re-seq #"[^a-zA-Z]" "")))

  (or (clojure.string/blank? "\t\t\t\t")
      (seq? (re-seq #"[^a-zA-Z]" "\t\t\t\t")))

  ;; Using a string from the test
  (or (clojure.string/blank? "Tom-ay-to, tom-aaaah-to.")
      (seq? (re-seq #"[^a-zA-Z]" "Tom-ay-to, tom-aaaah-to.")))

  ;; its a match, but according to the test it should fail.

  (or (clojure.string/blank? "\t\t\t\t")
      (seq? (re-seq #"\t" "\t\t\t\t")))

  (or (clojure.string/blank? "Tom-ay-to, tom-aaaah-to.")
      (seq? (re-seq #"\t" "Tom-ay-to, tom-aaaah-to.")))


  (re-matches #"\s*" "\t\t\t\t\t\t\t\t\t\t")

  (re-matches #"\s*" "hello")


  ;; Whatever else
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Search the tests for the string "Whatever."




  ;; Putting these responses together
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; A simple choice is to use a cond function
  ;; - the conditions should be applied in a certain order
  ;; (e.g check for capital letters and question mark before just capital letters)

  ;; To make the cond simpler, use a let expression to define the parsing of the string
  ;; Wrap the let in a function to pass in various strings to test the logic

  (defn response-for
    [phrase]
    (let [question? (= \? (last (clojure.string/trimr phrase)))

          yelling? (and (re-find #"[A-Z]" phrase)
                        (not (re-find #"[a-z]" phrase)))

          saying-nothing? (or (clojure.string/blank? phrase)
                              (seq? (re-seq #"\t" phrase)))]

      (cond

        (and yelling? question?) "Calm down, I know what I'm doing!"

        question? "Sure."

        yelling? "Whoa, chill out!"

        saying-nothing? "Fine. Be that way!"

        :whatever "Whatever."

        )))

  ) ;; End of rich comment block




;; Using re-matchers and regular expressions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment
  ;; The regular expressions cheatsheet from Mozilla Developer Network
  ;; was very helpful in understanding regular expressions
  ;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions/Cheatsheet

  ;; Using re-matchers, if the string matches the pattern,
  ;; then the string is returned,
  ;; otherwise nil is returned
  ;; [^a-z] matches if there are no lower case alphabetic characters
  ;; * any number of the proceeding pattern
  ;; [A-Z]+ any number of upper case alphabetic characters

  (re-matches #"[^a-z]*[A-Z]+[^a-z]*" "Hello")

  (re-matches #"[^a-z]*[A-Z]+[^a-z]*" "HELLO")

  (re-matches #"[^a-z]*[A-Z]+[^a-z]*" "ABC 1 2 3")


  ;; is this a question
  ;; \? matches the ? character
  ;; . matches any single character except line terminators (new line, carriage return)
  ;; .* matches any number of single characters one or more times
  ;; \s matches a single whitespace character
  ;; \s* matches multiple whitespace characters
  ;; $ is a boundary assertion so the pattern only matches the ? at the end of a string and not in the middle


  ;; Check if a question mark character is in the string
  (re-find #".*\?" "Okay if like my ? spacebar  quite a bit")

  ;; $ ensures the ? only matches if at the end of the string
  (re-find #".*\?$" "Okay if like my ? spacebar  quite a bit")


  ;; Match a single ? character with no other characters
  (re-matches #"\?" "?")
  ;; => "?"
  (re-matches #"\?" "Ready?")
  ;; => nil

  ;; Match a single ? character preceded by a single character
  (re-matches #".\?" "?")
  ;; => nil
  (re-matches #".\?" "R?")
  ;; => "R?"

  ;; Match any number of characters before the ?, but not after
  (re-matches #".*\?" "Ready?")
  ;; => "Ready?"
  (re-matches #".*\?" "?Ready")
  ;; => nil

  ;; Match any number of characters before the ? and the ? is at the end of the string
  (re-matches #".*\?$" "Okay if like my  spacebar  quite a bit?")
  ;; => "Okay if like my  spacebar  quite a bit?"

  ;; re-matches does not require the $ as there is an implicit boundary
  (re-matches #".*\?" "Okay if like my ? spacebar  quite a bit")

  ;; Match if there is a single space or space type character after the ?
  (re-matches #".*\?\s" "Okay if like my  spacebar  quite a bit? ")
  ;; => "Okay if like my  spacebar  quite a bit? "
  (re-matches #".*\?\s" "Okay if like my  spacebar  quite a bit?\n")
  ;; => "Okay if like my  spacebar  quite a bit?\n"
  (re-matches #".*\?\s" "Okay if like my  spacebar  quite a bit?  ")
  ;; => nil

  ;; Match if there are multiple space type characters after the ?
  (re-matches #".*\?\s*" "Okay if like my  spacebar  quite a bit?   ")
  ;; => "Okay if like my  spacebar  quite a bit?   "
  (re-matches #".*\?\s*" "Okay if like my  spacebar  quite a bit? \n ")
  ;; => "Okay if like my  spacebar  quite a bit? \n "

  ;; \s matches a single whitespace character
  ;; \s* matches multiple whitespace characters

  (re-matches #"\s*" "")
  (re-matches #"\s*" "  ")
  (re-matches #"\s*" "\t  ")

  ;; The results of the `re-matches` can be put into a cond

  (defn response-for
    [phrase]
    (let [;; A ? at the end of the phrase, not counting whitespace
          question (re-matches #".*\?\s*$" phrase)

          ;; No lower case characters, at least one upper case character
          yelling (re-matches #"[^a-z]*[A-Z]+[^a-z]*" phrase)

          ;; The entire string is whitespace
          silence (re-matches #"\s*" phrase)]

      (cond
        silence                "Fine. Be that way!"
        (and question yelling) "Calm down, I know what I'm doing!"
        question               "Sure."
        yelling                "Whoa, chill out!"
        :whatever              "Whatever.")))


  ) ;; End of rich comment block

(response-for "\t\t\t\t\t\t\t\t\t\t")


;; Alternative approaches
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;





;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (require '[clojure.string])

  (defn response-for [s] ;; <- arglist goes here
    (let [question           (= \? (last (clojure.string/trim s)))
          capitalized        (= s (clojure.string/upper-case s))
          special-characters (seq? (re-seq #"[^a-zA-Z]" s))
          whitespace         (or (= \  (last s)) (= \  (first s)))
          acronyms           (clojure.string/includes? s "DMV")]
      (cond
        ;; s is capitalised and ends with a ?
        (and capitalized
             question) "Calm down, I know what I'm doing!"

        ;; s ends in with a ?
        question "Sure."

        ;; s is capitalised
        (and capitalized
             (not special-characters)) "Whoa, chill out!"


        ;; s is just bob
        (and special-characters
             (not whitespace)
             (not acronyms)) "Fine. Be that way!"

        ;; anything else
        :else "Whatever.")))

  (response-for "Tom-ay-to, tom-aaaah-to.")

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment
  ;; https://exercism.io/tracks/clojure/exercises/bob/solutions/8ab163711bf4463188e187336ed95acc

  (let [filtered_string (apply str (re-seq #"[a-zA-Z?]+" s))
        only_letters    (apply str (re-seq #"[a-zA-Z]+" s))
        is_yelled       (and
                          (not (empty? only_letters))
                          (every? #(Character/isUpperCase %) only_letters))
        is_question     (ends-with? filtered_string "?")]

    (cond
      (= (count (trim s)) 0)                  "Fine. Be that way!"
      (and is_yelled is_question)             "Calm down, I know what I'm doing!"
      (and is_yelled (not is_question))       "Whoa, chill out!" ;; your code goes here
      (and (not is_yelled) (not is_question)) "Whatever."
      (and (not is_yelled) is_question)       "Sure."))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/tracks/clojure/exercises/bob/solutions/7cc715e118c64bfc9539be0c874ccc9a
  (defn capital? [s] (not-empty (re-find #"[A-Z!%^*@#$()]*" s)))
  (defn question? [s] (not-empty (re-find #"[\?]" s)))
  (defn clear-string [s] (clojure.string/replace s #"[\t\n\r]" " "))
  (defn str-digit? [s] (not-empty (re-find #"[0-9,]*" s)))

  (defn response-for [s]
    (let [coll (re-seq #"[^, ]+" (clear-string s))]
      (if (or (nil? coll)) "Fine. Be that way!"
          (if (question? (last coll))
            (if (= (count coll) (count (filter capital? coll)))
              "Calm down, I know what I'm doing!"
              "Sure.")
            (if (and (= (count coll) (+ (count (filter capital? coll)) (count (filter str-digit? coll))))
                     (not (= (count coll) (count (filter str-digit? coll)))))
              "Whoa, chill out!"
              "Whatever.")))))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (defn response-for [s] ;; <- arglist goes here
    ;; your code goes here
    (let [sx        (str/trim s)
          has-char? (not (str/blank? (str/replace sx #"(\d|\W)" "")))
          all-cap?  (and has-char? (= (str/upper-case sx) sx))]

      (cond
        (str/blank? sx)                        "Fine. Be that way!"
        (and all-cap? (str/ends-with? sx "?")) "Calm down, I know what I'm doing!"
        (str/ends-with? sx "?")                "Sure."
        all-cap?                               "Whoa, chill out!"
        ;;(or all-cap? (str/ends-with? sx "!")) "Whoa, chill out!"
        :else                                  "Whatever." ))

    )

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Brute force approach, a little hacky

  (defn response-for [s] ;; <- arglist goes here
    (let [clean-s (clojure.string/trim s)]
      (cond
        (= clean-s "")                                               "Fine. Be that way!"
        (and (>= (count (remove #(or (Character/isLowerCase %) (Character/isUpperCase %) (Character/isSpace %)) clean-s)) 2)
             (clojure.string/ends-with? clean-s "?"))                "Sure."
        (and (>= (count (filter #(Character/isDigit %) clean-s)) 1)
             (clojure.string/ends-with? clean-s "?"))                "Sure."
        (and (>= (count (filter #(Character/isDigit %) clean-s)) 1)
             (= (count (filter #(Character/isLetter %) clean-s)) 0)) "Whatever."
        (and (clojure.string/ends-with? clean-s "?")
             (= (count clean-s) 1))                                  "Sure."
        (and (clojure.string/ends-with? clean-s "?")
             (= (clojure.string/upper-case clean-s) clean-s))        "Calm down, I know what I'm doing!"
        (clojure.string/ends-with? clean-s "?")                      "Sure."
        (= (clojure.string/upper-case clean-s) clean-s)              "Whoa, chill out!"
        :else                                                        "Whatever.")))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;; Solution using string related functions
  ;; with Java Interoperability.

  (ns bob
    (:require [clojure.string :as str]))

  (defn- upper-case-if-letter? [ch]
    (or (not (Character/isLetter ch)) (Character/isUpperCase ch)))

  (defn yelling? [s]
    (when (some #(Character/isLetter %) s)
      (every? upper-case-if-letter? s)))

  (defn question? [s]
    (str/ends-with? (str/trimr s) "?"))

  (defn response-for [s]
    (cond
      (str/blank? s)                   "Fine. Be that way!"
      (and (yelling? s) (question? s)) "Calm down, I know what I'm doing!"
      (yelling? s)                     "Whoa, chill out!"
      (question? s)                    "Sure."
      :else                            "Whatever."))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Exercism community example

  (ns bob
    (:require [clojure.string :as string]))

  (defn- silence?    [phrase] (string/blank? phrase))

  (defn- question?   [phrase] (= \? (last phrase)))

  (defn- has-letter? [phrase] (some #(Character/isLetter (int %)) phrase))

  (defn- shouting?   [phrase] (and (= msg (string/upper-case phrase))
                                   (has-letter? phrase)))

  (defn- forceful-question? [phrase]
    (and (shouting? phrase) (question? phrase)))

  (defn response-for [input]
    (let [clean (string/trim input)]
      (cond
        (forceful-question? clean) "Calm down, I know what I'm doing!"
        (silence?  clean)          "Fine. Be that way!"
        (shouting? clean)          "Whoa, chill out!"
        (question? clean)          "Sure."
        :else                      "Whatever.")))


  ) ;; End of rich comment block





;; `clojure.string/blank?` checks for empty strings and whitespace, but does not provide a check for tabs, newlines and similar white space characters.  The regex patter `\s` covers all those white space characters.

;; While each binding in the let expression could be made into a `defn`, this add repetition in the `cond` conditions as each one would be a function call and argument.  Using the let binding, the cond is very simple.

;; In the scope of this project those functions are only used with `response-for`, making function definitions seem redundant.  If this project evolved more rules, then it may be useful to extract commonly used code into additional function definitions.



(defn response-for [s]
  (let [ends_with_?               (fn [x] (clojure.string/ends-with? x "?"))
        all_upper_case?           (fn [x] (= (clojure.string/upper-case x) x))
        all_upper_case_not_empty? (fn [x] (and (not (clojure.string/blank? x)) (all_upper_case? x)))
        s_trimmed                 (clojure.string/trim s)
        s_letters                 (apply str (filter (fn [x] (Character/isLetter x)) s_trimmed))]
    (cond
      (empty? s_trimmed)                    "Fine. Be that way!"
      (ends_with_? s_trimmed)               (if (all_upper_case_not_empty? s_letters) "Calm down, I know what I'm doing!" "Sure.")
      (all_upper_case_not_empty? s_letters) "Whoa, chill out!"
      :else                                 "Whatever.")
    )
  )



(defn response-for [s]
  (let [ends_with_?               #(clojure.string/ends-with? % "?")
        all_upper_case?           #(= (clojure.string/upper-case %) %)
        all_upper_case_not_empty? #(and (not (clojure.string/blank? %)) (all_upper_case? %))
        s_trimmed                 (clojure.string/trim s)
        s_letters                 (apply str (filter #(Character/isLetter %) s_trimmed))]
    (cond
      (empty? s_trimmed)                    "Fine. Be that way!"
      (ends_with_? s_trimmed)               (if (all_upper_case_not_empty? s_letters) "Calm down, I know what I'm doing!" "Sure.")
      (all_upper_case_not_empty? s_letters) "Whoa, chill out!"
      :else                                 "Whatever.")
    )
  )


(defn response-for [s]
  (let [ends_with_?               #(clojure.string/ends-with? % "?")
        all_upper_case?           #(= (clojure.string/upper-case %) %)
        all_upper_case_not_empty? #(and (not (clojure.string/blank? %)) (all_upper_case? %))
        s_trimmed                 (clojure.string/trim s)
        s_letters                 (apply str (filter #(Character/isLetter %) s_trimmed))]
    (cond
      (empty? s_trimmed)                    "Fine. Be that way!"
      (ends_with_? s_trimmed)               (if (all_upper_case_not_empty? s_letters) "Calm down, I know what I'm doing!" "Sure.")
      (all_upper_case_not_empty? s_letters) "Whoa, chill out!"
      :else                                 "Whatever.")
    )
  )


(defn response-for [phrase]
  (let [phrase-trimmed            (clojure.string/trim phrase)
        characters                (apply str (filter #(Character/isLetter %) phrase-trimmed))
        question?                 clojure.string/ends-with? phrase-trimmed "?"
        all-upper-case?           (= (clojure.string/upper-case characters) characters)
        all-upper-case-not-empty? (and (not (clojure.string/blank? characters)) all-upper-case? ) ]
    (cond
      empty?                                    "Fine. Be that way!"
      (and question? all-upper-case-not-empty?) "Calm down, I know what I'm doing!"
      question?                                 "Sure."
      all-upper-case-not-empty?                 "Whoa, chill out!"
      :else                                     "Whatever.")))




(defn response-for [s]
  (let [s_trimmed   (clojure.string/trim s)
        ends_with_? (clojure.string/ends-with? s_trimmed "?")]
    (cond
      empty?                                      "Fine. Be that way!"
      (and ends_with_? all_upper_case_not_empty?) "Calm down, I know what I'm doing!"
      all_upper_case_not_empty?
      all_upper_case_not_empty?                   s_letters "Whoa, chill out!"
      :else                                       "Whatever.")
    )
  )
