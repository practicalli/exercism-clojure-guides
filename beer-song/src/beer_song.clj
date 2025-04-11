(ns beer-song
  (:require clojure.string))

;; To get the right strings based on the number bottles left,
;; a cond statement is relatively simple
;; There are significant context differences preventing a terser solution

;; starting point
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#_(defn verse
    "Returns the nth verse of the song."
    [num]
    ;; your code here
    )

#_(defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start])
    ([start end]))

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

#_(range
   10)
#_(range 2 11)
#_(range 100 0)
#_(range 100 0 -1)
#_(range 99 -1 -1)

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

(map verse (range 3 -1 -1))
;; => ("3 bottles of beer on the wall, 3 bottles of beer.\nTake one down and pass it around, 2 bottles of beer on the wall.\n" "2 bottles of beer on the wall, 2 bottles of beer.\nTake it down and pass it around, 1 more bottle of beer on the wall.\n" "1 bottle of beer on the wall, 1 bottle of beer.\nTake it down and pass it around, no more bottles of beer on the wall.\n" "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n")

#_(sing 8 6)
;; => ("8 bottles of beer on the wall, 8 bottles of beer.\nTake one down and pass it around, 7 bottles of beer on the wall.\n"
;;     "7 bottles of beer on the wall, 7 bottles of beer.\nTake one down and pass it around, 6 bottles of beer on the wall.\n"
;;     "6 bottles of beer on the wall, 6 bottles of beer.\nTake one down and pass it around, 5 bottles of beer on the wall.\n")

;; a list of strings is being returned, rather than a complete string.
;; apply str will fix this

(defn sing
  "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
  ([start]
   (map verse (range start -1 -1)))
  ([start end]
   (apply str (map verse (range start (dec end) -1)))))

;; to minimise code, change the single argument branch to call the two argument branch

(defn sing
  "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
  ([start]
   (sing start -1))
  ([start end]
   (apply str (map verse (range start (dec end) -1)))))

;; There is an extra newline character between each verse

(defn sing
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
   (clojure.string/join "\n" (map verse (range start (dec end) -1)))))

;; (clojure.string/join "," '("a" "b" "c"))

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

;; https://exercism.io/mentor/solutions/78229949b09243a7bfe8610a7008874f

  (ns beer-song)

  (defn verse-builder [verse] (fn [num] (str (verse num))))

  (def multi-bottle-verse (verse-builder
                           #(str %
                                 " bottles of beer on the wall, "
                                 %
                                 " bottles of beer.\n"
                                 "Take one down and pass it around, "
                                 (- % 1)
                                 (if (= % 2)
                                   " bottle"
                                   " bottles")
                                 " of beer on the wall.\n")))

  (def single-bottle-verse (verse-builder
                            #(str %
                                  " bottle of beer on the wall, "
                                  %
                                  " bottle of beer.\n"
                                  "Take it down and pass it around, no more bottles of beer on the wall.\n")))

  (def zero-bottles-verse (verse-builder
                           #(str "No more bottles of beer on the wall, no more bottles of beer.\n"
                                 "Go to the store and buy some more, "
                                 (+ % 99)
                                 " bottles of beer on the wall.\n")))

  (defn verse
    "Returns the nth verse of the song."
    [num]
    (cond
      (= num 0) (zero-bottles-verse num)
      (= num 1) (single-bottle-verse num)
      :else     (multi-bottle-verse num)))

  (defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (sing start 0))
    ([start end]
     (if (= start end)
       (verse end)
       (str (verse start)
            "\n"
            (sing (- start 1) end)))))

  (sing 99)
  (verse 2)





  (ns beer-song)

  (defn verse-builder [verse] (fn[num] (str (verse num))))

  (def multi-bottle-verse (verse-builder
                            #(str %
                                  " bottles of beer on the wall, "
                                  %
                                  " bottles of beer.\n"
                                  "Take one down and pass it around, "
                                  (dec %)
                                  (if (= % 2)
                                    " bottle"
                                    " bottles")
                                  " of beer on the wall.\n"
                                  )))

  (defn multi-bottle-verse2
    [num]
    (format "%d bottles of beer on the wall, %d bottles of beer.\nTake one down and pass it around, %d bottles of beer on the wall.\n" num num (dec num)))

  (def single-bottle-verse "1 bottle of beer on the wall, 1 bottle of beer.\nTake it down and pass it around, no more bottles of beer on the wall.\n")
  (def zero-bottle-verse "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n")

  (defn verse
    [num]
    (case num
      0 zero-bottle-verse
      1 single-bottle-verse
      (multi-bottle-verse num)))

  (defn sing
    ([start]
     (sing start 0))
    ([start end]
     (map verse (range start (dec end) -1))))

  (verse 0)
  (verse 1)
  (verse 9)
  (sing 3 0)

  (map (comp clojure.string/join "\n" verse) (range 0 3)))

;; End of rich comment block

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns beer-song
    (:require clojure.string))

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
      :else
      (str num " bottles of beer on the wall, " num " bottles of beer.\n"
           "Take one down and pass it around, " (dec num) " bottles of beer on the wall.\n")))

  (defn sing
    "Given a start and an optional end, returns all verses in this interval. If
  end is not given, the whole song from start is sung."
    ([start]
     (sing start 0))
    ([start end]
     (clojure.string/join "\n" (map verse (range start (dec end) -1))))))

   ;; End of rich comment block
