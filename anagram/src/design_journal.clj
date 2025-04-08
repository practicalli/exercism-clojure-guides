(ns design-journal)

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;;;;;;;;
  ;; Well done using comp and filter.
  ;; Perhaps the function passed to filter could be an auxiliary function. It may read better.
  ;; Using partial can be helpful to pass some args to the function.

  (ns anagram)
  (def lower clojure.string/lower-case)


  (defn anagrams-for [word prospect-list]
    (let [w ((comp sort lower) word)]
      (filter (fn [prospect] (let [p (lower prospect)]
                               (and (not= (lower word) p)
                                    (= w (sort p))))) prospect-list)))


  ;;;;;;;;
  ;; take advantage that word doesn't change and calculate beforehand the sorted and lowercase version.

  (ns anagram)
  (def lower clojure.string/lower-case)

  (defn- anagram? [word prospect]
    (let [w ((comp sort lower) word)
          p (lower prospect)]
      (and (not= (lower word) p)
           (= w (sort p)))))

  (defn anagrams-for [word prospect-list]
    (filter (partial anagram? word) prospect-list))


  ;;;;;;;;
  ;; make word lower and sort it only once in the main function

  (ns anagram)
  (def lower clojure.string/lower-case)

  (defn- anagram? [word word-lower word-sorted prospect]
    (let [p (lower prospect)]
      (and (not= word-lower p)
           (= word-sorted (sort p)))))

  (defn anagrams-for [word  prospect-list]
    (let [word-lower  (lower word)
          word-sorted (sort word-lower)]
      (filter (partial anagram? word word-lower word-sorted) prospect-list)))


  ) ;; End of rich comment block
