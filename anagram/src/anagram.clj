(ns anagram)


(defn word->character-set [word]
  (into set (map str (seq word))))

(defn anagrams-for [word prospect-list] 
  (let [word-character-set (word->character-set word)
        prospect-list-character-sets (map word->character-set prospect-list)]
  (filter word-character-set prospect-list-character-sets)))
