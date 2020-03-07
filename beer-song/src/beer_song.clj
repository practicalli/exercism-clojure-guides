(ns beer-song
  (:require clojure.string))

;; To get the right strings based on the number bottles left,
;; a cond statement is relatively simple
;; There are significant context differences preventing a terser solution

#_(defn verse
    "Returns the nth verse of the song."
    [num]
    (cond
      (= num 0)
      (str "No more bottles of beer on the wall, no more bottles of beer.\n"
           "Go to the store and buy some more, 99 bottles of beer on the wall.\n")

      (= num 1)
      (str "1 bottle of beer on the wall, 1 bottle of beer.\n"
           "Take it down and pass it around, no more bottles of beer on the wall.\n")

      :otherwise
      (str num " bottles of beer on the wall, " num " bottles of beer.\n"
           "Take one down and pass it around, " (dec num) " bottles of beer on the wall.\n")))

#_(verse 0)
#_(verse 1)
#_(verse 8)


;; generating a sequence of numbers

#_(range 10)
#_(range 2 11)
#_(range 100 0)
#_(range 100 0 -1)
#_(range 100 -1 -1)

#_(defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (map verse (range start -1 -1)))
    ([start end]
     (map verse (range start (dec end) -1))))


;; need to fix the condition for 2

(defn verse
  "Returns the nth verse of the song."
  [num]
  (cond
    (= num 0)
    (str "No more bottles of beer on the wall, no more bottles of beer.\n"
         "Go to the store and buy some more, 99 bottles of beer on the wall.\n")

    (= num 1)
    (str "1 bottle of beer on the wall, 1 bottle of beer.\n"
         "Take it down and pass it around, no more bottles of beer on the wall.\n")

    (= num 2)
    (str "2 bottles of beer on the wall, 2 bottles of beer.\n"
         "Take one down and pass it around, 1 bottle of beer on the wall.\n")

    :otherwise
    (str num " bottles of beer on the wall, " num " bottles of beer.\n"
         "Take one down and pass it around, " (dec num) " bottles of beer on the wall.\n")))

#_(map verse (range 3 -1 -1))
;; => ("3 bottles of beer on the wall, 3 bottles of beer.\nTake one down and pass it around, 2 bottles of beer on the wall.\n" "2 bottles of beer on the wall, 2 bottles of beer.\nTake it down and pass it around, 1 more bottle of beer on the wall.\n" "1 bottle of beer on the wall, 1 bottle of beer.\nTake it down and pass it around, no more bottles of beer on the wall.\n" "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n")

#_(sing 8 6)
;; => ("8 bottles of beer on the wall, 8 bottles of beer.\nTake one down and pass it around, 7 bottles of beer on the wall.\n"
;;     "7 bottles of beer on the wall, 7 bottles of beer.\nTake one down and pass it around, 6 bottles of beer on the wall.\n"
;;     "6 bottles of beer on the wall, 6 bottles of beer.\nTake one down and pass it around, 5 bottles of beer on the wall.\n")


;; a list of strings is being returned, rather than a complete string.
;; apply str will fix this


#_(defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (map verse (range start -1 -1)))
    ([start end]
     (apply str (map verse (range start (dec end) -1)))))


;; to minimise code, change the single argument branch to call the two argument branch

#_(defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (sing start -1))
    ([start end]
     (apply str (map verse (range start (dec end) -1)))))


;; There is an extra newline character between each verse

#_(defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (sing start -1))
    ([start end]
     (interpose "\n" (map verse (range start (dec end) -1)))))

;; the result still needs to be a string, so we use clojure.string/join

(defn sing
  "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
  ([start]
   (sing start 0))
  ([start end]
   (clojure.string/join "\n" (map verse (range start (dec end) -1)) )))
