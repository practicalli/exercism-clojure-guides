(ns design-journal)

;; Implement run-length encoding and decoding.

;; Run-length encoding (RLE) is a simple form of data compression, where runs (consecutive data elements) are replaced by just one data value and count.

;; For example we can represent the original 53 characters with only 13.

;; "WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWB"  ->  "12WB12W3B24WB"

;; RLE allows the original data to be perfectly reconstructed from the compressed data, which makes it a lossless data compression.

;; "AABCCCDEEEE"  ->  "2AB3CD4E"  ->  "AABCCCDEEEE"

;; For simplicity, you can assume that the unencoded string will only contain the letters A through Z (either lower or upper case) and whitespace. This way data to be encoded will never contain any numbers and numbers inside data to be decoded always represent the count for the following character.
;; Source

;; Wikipedia https://en.wikipedia.org/wiki/Run-length_encoding
;; Submitting Incomplete Solutions

;; It's possible to submit an incomplete solution so you can see how others have completed the exercise.


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns run-length-encoding
    (:require [clojure.string]))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (clojure.string/join (->> plain-text
                              (partition-by identity)
                              (map #(str (when (< 1 (count %)) (count %)) (first %))))))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (->> cipher-text
         (re-seq #"(\d*)(\D)")
         (mapcat (fn [[_ count-str ch]]
                   (let [count (if (= "" count-str) 1 (Integer/parseInt count-str))]
                     (repeat count ch))))
         (clojure.string/join)))


  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  (ns run-length-encoding
    (:require [clojure.string :as str]))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (->> (partition-by identity plain-text)
         (mapcat (juxt count first))
         (remove #(= 1 %))
         (apply str)))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (->> (re-seq #"(\d+)?(\D)" cipher-text)
         (mapcat #(let [[_ second third] %]
                    (repeat (Long/parseLong (or second "1")) third)))
         (apply str)))


  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment



  (ns run-length-encoding
    (:require [clojure.string :as str]))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (->> (partition-by identity plain-text)
         (mapcat (juxt count first))
         (remove #(= 1 %))
         (apply str)))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (->> (re-seq #"(\d+)?(\D)" cipher-text)
         (mapcat #(let [[_ second third] %]
                    (repeat (Long/parseLong (or second "1")) third)))
         (apply str)))

  ) ;; End of rich comment block




;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/mentor/solutions/6fd0d6ef29ea4befb8790120208836ed

  (ns run-length-encoding
    (:require [clojure.string :as str]))

  (defn- run-length-encode-acc
    [chars acc count prev-char]
    (cond
      (empty? chars)
      (str/join (conj acc (if (> count 1) count "") prev-char))
      (= prev-char (first chars))
      (recur (rest chars) acc (inc count) prev-char)
      :else
      (recur (rest chars)
             (conj acc (if (> count 1) count "") prev-char)
             1
             (first chars))))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (if (empty? plain-text)
      ""
      (run-length-encode-acc plain-text [] 0 (first plain-text))))

  (defn- run-length-decode-acc
    [cipher-text run-acc result-acc]
    (cond
      (empty? cipher-text)
      (str/join result-acc)
      (Character/isDigit (first cipher-text))
      (recur
        (rest cipher-text)
        (+ (- (int (first cipher-text)) (int \0)) (* 10 run-acc))
        result-acc)
      :else
      (recur
        (rest cipher-text)
        0
        (concat result-acc
                (repeat (if (= 0 run-acc) 1 run-acc)
                        (first cipher-text))))))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (if (empty? cipher-text)
      ""
      (run-length-decode-acc cipher-text 0 [])))

  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; https://exercism.io/mentor/solutions/ab844328f9084268a15a89271ed144ff

  ;; Mentor comments
  ;; For recursion Clojure has a special keyword that implement TCO (Tail Call Optimization). It called recur and you can use to call the same function or with loop.
  ;; Having said that (II) before doing custom recursion is always a good idea to take a look at existing functions.
  ;; Using partition-by and map can help you encoding the string.
  ;; For decoding, if you capture the groups in the expression that can simplify a bit the expansion logic.
  ;; Also you can use #"\D" for non digits.


  (ns run-length-encoding)

  (defn- encode-vec
    "Given a vec [12 \\A 3 \\B 1 \\C] returns \"12A3BC\".
   Note that the integer 1 got removed as per the specs."

    [coll]
    {:pre [(vector? coll)]}
    (let [ones-removed (filterv #(or (char? %) (> % 1)) coll)]
      (apply str ones-removed)))

  (defn- text->vec
    "Given a string, returns a vector with Run Length Encoding components:
   \"AAABBC\" -> [3 \\A 2 \\B 1 \\C]"

    [text current-char current-count encoded]
    {:pre [(seq? text)
           (not (nil? current-char))
           (pos? current-count)
           (vector? encoded)]}
    (cond
      (empty? text)
      (conj encoded current-count current-char)

      (= (first text) current-char)
      (text->vec (rest text) current-char (inc current-count) encoded)

      :else
      (text->vec (rest text) (first text) 1 (conj encoded current-count current-char))))


  (defn run-length-encode
    "encodes a string using run-length-encoding"
    [plain-text]
    {:pre [(string? plain-text)]}
    (if (empty? plain-text)
      plain-text
      (encode-vec
        (text->vec (rest plain-text) (first plain-text) 1 []))))

  (defn- materialize
    "Recursively decodes a run-length-encoded vector of items into `decoded`"
    [decoded coll]
    (let [first-item (first coll)
          first-char (first first-item)]
      (cond
        (nil? first-item) decoded

        (or (Character/isSpace first-char) (Character/isLetter first-char))
        (materialize (conj decoded first-item) (rest coll))

        :else ; first-item is an integer followed by a character
        (let
            [second-item         (second coll)
             howmany             (Integer/parseInt first-item)
             second-materialized (apply str (repeat howmany second-item))]
          (materialize (conj decoded second-materialized) (nthrest coll 2))))))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    {:pre [string? cipher-text]}
    (->> cipher-text
         (re-seq #"\d+|[a-zA-Z ]")
         (materialize [])
         (apply str)))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Mentor comments
  ;; Use partiton-by for splitting the sequence
  ;; use re-matches or re-find with regex for the decoding
  ;; https://exercism.io/mentor/solutions/6fd0d6ef29ea4befb8790120208836ed

  (ns run-length-encoding
    (:require [clojure.string :as str]))

  (defn- run-length-encode-acc
    [chars acc count prev-char]
    (cond
      (empty? chars)
      (str/join (conj acc (if (> count 1) count "") prev-char))
      (= prev-char (first chars))
      (recur (rest chars) acc (inc count) prev-char)
      :else
      (recur (rest chars)
             (conj acc (if (> count 1) count "") prev-char)
             1
             (first chars))))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (if (empty? plain-text)
      ""
      (run-length-encode-acc plain-text [] 0 (first plain-text))))

  (defn- run-length-decode-acc
    [cipher-text run-acc result-acc]
    (cond
      (empty? cipher-text)
      (str/join result-acc)
      (Character/isDigit (first cipher-text))
      (recur
        (rest cipher-text)
        (+ (- (int (first cipher-text)) (int \0)) (* 10 run-acc))
        result-acc)
      :else
      (recur
        (rest cipher-text)
        0
        (concat result-acc
                (repeat (if (= 0 run-acc) 1 run-acc)
                        (first cipher-text))))))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (if (empty? cipher-text)
      ""
      (run-length-decode-acc cipher-text 0 [])))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns run-length-encoding
    (:require [clojure.string :as str]))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (->> (partition-by identity plain-text)
         (mapcat (juxt count first))
         (remove #(= 1 %))
         (apply str)))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (->> (re-seq #"(\d+)?(\D)" cipher-text)
         (mapcat #(let [[_ second third] %]
                    (repeat (Long/parseLong (or second "1")) third)))
         (apply str)))


  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns run-length-encoding)

  (defn- encode [[fst & rst :as run]]
    (cond->> fst
      rst (str (+ 1 (count rst)))))

  (defn run-length-encode
    "encodes a string with run-length-encoding"
    [plain-text]
    (->> plain-text
         (partition-by identity)
         (map encode)
         (apply str)))

  (defn- decode [[_ len-ind ch]]
    (apply str (cond->> ch
                 len-ind (repeat (Integer/parseInt len-ind)))))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    (->> cipher-text
         (re-seq #"(\d+)?(\D)")
         (map decode)
         (apply str)))

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (ns run-length-encoding)

  (defn run-length-encode
    "encodes a string using run-length-encoding"
    [plain-text]
    {:pre [(string? plain-text)]}
    (if (empty? plain-text)
      plain-text
      (->> plain-text
           (partition-by identity)
           (mapv #(vector (count %) (first %)))
           flatten
           (remove #(= 1 %))
           (apply str))))

  (defn one-if-empty [howMany] (if (empty? howMany) "1" howMany))

  (defn run-length-decode
    "decodes a run-length-encoded string"
    [cipher-text]
    {:pre [string? cipher-text]}
    (->> cipher-text
         (re-seq #"(\d*)([a-zA-Z ])")
         (map #(vector (Integer/parseInt (one-if-empty (second %))) (nth % 2)))
         (map #(apply str (repeat (first %) (second %))))
         (apply str)))

  ) ;; End of rich comment block
