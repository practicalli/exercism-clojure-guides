(ns spiral-matrix)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Taking a lateral thinking approach
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; For a 3x3  matrix we have the following rows.
;; 1 2 3
;; 4 5 6
;; 7 8 9

;; The spiral version of the matrix is
;; 1 2 3
;; 8 9 4
;; 7 6 5

;; Which gives a sequence of
;; [1 2 3 8 9 4 7 6 5]

;; Looking at the relative positions of the numbers

;; 1 goes into position 1
;; 2 goes into position 2
;; 3 goes into position 3
;; 4 goes into position 6
;; 5 goes into position 9
;; 6 goes into position 8
;; 7 goes into position 6
;; 8 goes into position 3
;; 9 goes into position 4

;; So if we can generate the following sequence
;; 1 2 3 6 9 8 6 3 4

;; then we can map them to incremental numbers in the matrix
;; and get an indexed set of values that can generate the spiral from.

;; 1 2 3 6 9 8 6 3 4  ;; location on grid
;; 1 2 3 4 5 6 7 8 9  ;; value at location


(defn spiral [n]
  (let [progression (cycle [1 n -1 (- n)])]
    (->> (range (dec n) 0 -1)
         (mapcat #(repeat 2 %))
         (cons n)
         (mapcat #(repeat %2 %) progression)
         (reductions +)
         (map vector (range 1 (inc (* n n))))
         (sort-by second)
         (map first)
         (partition n))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Very simplistic approach

  ;; Passing first two tests
  ;; `spiral-matrix-of-0` and `spiral-matrix-of-1`
  #_(defn spiral [size]
      (let [sequence (partition size (take (* size size) (rest (range))))]
        sequence)
      )


  ;; Passes test `spiral-matrix-of-2`
  ;; Fails all other tests
  #_(defn spiral
      [size]
      (let [sequence (partition size (take (* size size) (rest (range))))]
        (list
          (first sequence)
          (reverse (second sequence)))))



  ) ;; End of rich comment block


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Recursive function approach

  (defn rotate [matrix]
    (apply map list (reverse matrix)))

  (defn spiral-matrix [size new-size start]
    (let [row (list (range start (+ start size)))]
      (if (= 1 new-size)
        row
        (concat row
                (rotate (spiral-matrix (dec new-size ) size (+ start size)))))))

  (defn spiral [size]
    (if (> size 0)
      (spiral-matrix size size 1)
      '()))


  ) ;; End of rich comment block



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Very manual approach

  (ns spiral-matrix
    (:require [clojure.pprint :refer [pprint]]))


  ;; strategy:
  ;; Example: spriral 5
  ;; 5-to-right, 1 2 3 4 5
  ;; 4-down 6 7 8 9,
  ;; 4-left 10 11 12 13,
  ;; 3-up 14 15 16,
  ;; 3-right 17 18 19
  ;; 2-d 20 21,
  ;; 2-l 22 23
  ;; 1-u 24,
  ;; 1r 25
  ;; general: right(n) down(n-1) left(n-1) up(n-2) start again --> right(n-2)
  ;; Eg. 3 --> 3r (123) 2d (45) 2l (67) 1u (8) 1r (9)
  (defn spiral [n]
    (let [segments (as-> (range (dec n) 0 -1) col
                     (map #(vector % %) col)
                     (flatten (conj col n)))

          directions (->> (repeat '(1 0 0 1 -1 0 0 -1))
                          (take (Math/ceil (/ (count segments) 4)))
                          flatten
                          (partition 2))

          val-partitions (drop 1
                               (reduce
                                 #(conj %1
                                        (range
                                          (inc (last (last %1)))
                                          (inc (+ (last (last %1)) %2))))
                                 [[0]] segments))

          steps (partition 3 (flatten
                               (map (fn [d s]
                                      (flatten
                                        (map (fn [v] [d v]) s)))
                                    directions val-partitions)))

          x (atom -1)

          y (atom 0)

          matrix (atom (vec (repeat n (vec (repeat n 0)))))

          matrix (reduce #(do
                            (swap! x + (first %2))
                            (swap! y + (second %2))
                            (reset! %1 (assoc-in @%1 [@y @x] (last %2)))
                            %1)
                         matrix steps)]
      @matrix))

  ) ;; End of rich comment block
