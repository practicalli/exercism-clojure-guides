(ns phone-number)

;; ---------------------------------------------------------
(def phone-characters
  #{\. \( \) \@})
;; ---------------------------------------------------------

;; ---------------------------------------------------------
(defn clean-number [num-string phone-characters]
  (apply str (remove phone-characters num-string)))

(defn number [num-string]

  ((cond
     (= 11 (count num-string)) (clean-number num-string phone-characters)
     :else nil)))

(defn area-code [num-string]) ;; <- arglist goes here
  ;; your code goes here

(defn pretty-print [num-string]) ;; <- arglist goes here
  ;; your code goes here

;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; REPL experiements

(comment
  ;; A set can be used as a function with remove
  (remove #{\@} "jenny@jetpack"))
