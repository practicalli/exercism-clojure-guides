(ns bob
  (:require clojure.string))

;; This could be the very basics of a text based adventure game or a very unintelligent chatbot

;; Bob is a lackadaisical teenager. In conversation, his responses are very limited.
;; Bob answers 'Sure.' if you ask him a question.
;; He answers 'Whoa, chill out!' if you yell at him.
;; He answers 'Calm down, I know what I'm doing!' if you yell a question at him.
;; He says 'Fine. Be that way!' if you address him without actually saying anything.
;; He answers 'Whatever.' to anything else.


;; Using re-matches and regular expression
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
