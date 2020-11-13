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
