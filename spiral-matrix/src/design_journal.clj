(ns design-journal)

;; The challenge
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Given the size, return a square matrix of numbers in spiral order.

;; The matrix should be filled with natural numbers, starting from 1 in the top-left corner, increasing in an inward, clockwise spiral order, like these examples:
;; Spiral matrix of size 3

;; 1 2 3
;; 8 9 4
;; 7 6 5

;; Spiral matrix of size 4

;; 1  2  3 4
;; 12 13 14 5
;; 11 16 15 6
;; 10  9  8 7

;; Sounds easy, right :)


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Generating a sequence of numbers
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; A matrix can be thought of as a sequence of numbers
  ;; printed out in a rectangle shape

  ;; Create a sequence of numbers from 1 to 9

  (range 1 10)
  ;; => (1 2 3 4 5 6 7 8 9)

  ;; to make it more general, use range as a lazy infinite sequence
  ;; taking only the values required
  (take 9 (range))
  ;; => (0 1 2 3 4 5 6 7 8)

  (take 9 (range 1 10))
  ;; => (1 2 3 4 5 6 7 8 9)

  ;; Or to keep it lazy we could try rest

  (take 9 (rest (range)))


  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Reviewing the tests for the matrix structure
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; In the test code the matrices are represented by
  ;; a collection of sequences, with each sequence a row of the matrix


  ;; To make a specific grouping we could use partition

  (partition 3 (take 9 (rest (range))))
  ;; => ((1 2 3) (4 5 6) (7 8 9))


  ;; Sequence for a 3 x 3 matrix
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; The starting matrix is
  ;; 1 2 3
  ;; 4 5 6
  ;; 7 8 9

  ;; The spiral version of the matrix is
  ;; 1 2 3
  ;; 8 9 4
  ;; 7 6 5

  ;; Which gives a sequence of
  [1 2 3 8 9 4 7 6 5]


  ;; Logic for a 3 x 3 matrix
  ;; Take the first row as it is
  ;; second row is the last two digits of the last row, plus the first digit from the second row
  ;; third row is the first digit from the last row, plus the last two digits of the second row reversed


  ;; A function that takes a number and returns a sequence of sequences
  ;; that should represent a matrix

  (defn spiral
    [size]
    (let [sequence (partition size (take (* size size) (rest (range))))]
      sequence))

  (spiral 1)
  ;; This passes the first two tests, however, the third test fails
  ;; as the numbers now need to be generated in reverse order


  ;; ((1 2) (4 3))


  (defn spiral
    [size]
    (let [sequence (partition size (take (* size size) (rest (range))))]
      (list
        (first sequence)
        (reverse (second sequence)))))


  (spiral 2)
  ;; => ((1 2) (4 3))

  ;; So the third test should now pass

  (spiral 1)
  ;; => ((1) ())


  ;; This approach looks like its heading for loop recur or maybe recursive function

  ;; Lets try thinking laterally



  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Thinking laterally
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
  [1 2 3 8 9 4 7 6 5]

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



  ;; Creating mathematical pattern for generating the matrix
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Generate a pattern that defines the order in which
  ;; incremental numbers should be placed to form a spiral.
  ;; An index for where each consecutive number should be placed

  ;; Creating reverse range starting from one less than the matrix row size
  (range (dec 3) 0 -1)
  ;; => (2 1)

  ;; duplicate the sequence
  (mapcat #(repeat 2 %) (range (dec 3) 0 -1))
  ;; => (2 2 1 1)

  ;; Add the row-size to the front
  (cons 3 (mapcat #(repeat 2 %) (range (dec 3) 0 -1)))
  ;; => (3 2 2 1 1)


  ;; An example with a 4x4 matrix
  ;; (cons 4 (mapcat #(repeat 2 %) (range (dec 4) 0 -1)))
  ;; => (4 3 3 2 2 1 1)


  ;; Create a cyclical pattern that will be used to generate numbers
  ;; using a fairly simple mathematical progression (I think that is the right term)
  ;; using 1 size-of-row -1 (- size-of-row)
  ;; cycle will lazily generate an infinite sequence from this pattern
  ;; the pattern will be as big as the total size of the matrix
  (take 9 (cycle [1 3 -1 (- 3)]))
  ;; => (1 3 -1 -3 1 3 -1 -3 1 3)


  ;; Joint these two patterns together to generate a sequence
  (mapcat #(repeat %2 %)
          '(1 3 -1 -3 1 3 -1 -3 1 3)
          '(3 2 2 1 1) )
  ;; => (1 1 1 3 3 -1 -1 -3 1)


  ;; combining the code
  (mapcat #(repeat %2 %)
          (cycle [1 3 -1 (- 3)])
          (cons 3 (mapcat #(repeat 2 %) (range (dec 3) 0 -1))))
  ;; => (1 1 1 3 3 -1 -1 -3 1)


  ;; Now we have the pattern that can be used to generate
  ;; the sequence of numbers that makes the spiral matrix


  ;; Generating the spiral sequence
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  ;; Reductions is like reduce that shows its working...
  ;; if the above pattern is reduced with `+` then a single value is returned
  ;; with reductions each value of the reduce iteration is returned

  (reduce + '(1 1 1 3 3 -1 -1 -3 1))
  ;; => 5

  (reductions + '(1 1 1 3 3 -1 -1 -3 1))
  ;; => (1 2 3 6 9 8 7 4 5)


  ;; Putting the code together so far...
  (reductions +
              (mapcat #(repeat %2 %)
                      (cycle [1 3 -1 (- 3)])
                      (cons 3 (mapcat #(repeat 2 %) (range (dec 3) 0 -1)))) )


  ;; We now have a patter than

  ;; Add an incremental index to each value in the sequence making an ordered tuple
  (map vector (range 1 (inc (* 3 3))) '(1 2 3 6 9 8 7 4 5))
  ;; => ([1 1] [2 2] [3 3] [4 6] [5 9] [6 8] [7 7] [8 4] [9 5])


  ;; sort by the value in the matrix rather than the incremental index
  ;; to give the right shape to the spiral matrix

  (sort-by second '([1 1] [2 2] [3 3] [4 6] [5 9] [6 8] [7 7] [8 4] [9 5]))
  ;; => ([1 1] [2 2] [3 3] [8 4] [9 5] [4 6] [7 7] [6 8] [5 9])


  ;; Extract all the values for the matrix to create a sequence
  (map first '([1 1] [2 2] [3 3] [8 4] [9 5] [4 6] [7 7] [6 8] [5 9]))
  ;; => (1 2 3 8 9 4 7 6 5)

  ;; Partition by the sequence by the size of the matrix
  (partition 3 '(1 2 3 8 9 4 7 6 5))
  ;; => ((1 2 3) (8 9 4) (7 6 5))

  ;; And we have our answer...


  ;; Put this into a function

  (defn spiral [size]
    (let [progression (cycle [1 size -1 (- size)])]
      (partition
        size
        (map
          first
          (sort-by
            second
            (map
              vector
              (range 1 (inc (* size size)))
              (reductions
                +
                (mapcat #(repeat %2 %)
                        progression
                        (cons size
                              (mapcat #(repeat 2 %)
                                      (range (dec size) 0 -1)))))))))))


  (spiral 3)


  ;; Refactor the function with a threading macro
  (defn spiral [size]
    (let [progression (cycle [1 size -1 (- size)])]
      (->> (range (dec size) 0 -1)
           (mapcat #(repeat 2 %))
           (cons size)
           (mapcat #(repeat %2 %) progression )
           (reductions +)
           (map vector (range 1 (inc (* size size))))
           (sort-by second)
           (map first)
           (partition size))))

  (spiral 3)


  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  ;; Recursive function - spiral-matrix

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

  (spiral 3)

  ) ;; End of rich comment block



;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment


  (def rotation
    (cycle '((0 1) (1 0) (0 -1) (-1 0))))

  (defn runs [n]
    (->> (range n -1 -1) (partition 2 1) flatten))

  (defn directions [n]
    (next (mapcat #(repeat %1 %2) (runs n) rotation)))

  (defn walk-from [indices dir]
    (conj indices (map + (last indices) dir)))

  (defn positions [n]
    (reduce walk-from ['(0 0)] (directions n)))

  (defn position-value-list->matrix [coll]
    (->> coll
         (sort-by (comp (juxt first second) second))
         (partition-by (comp first last))))

  (defn spiral [n]
    (->> (positions n)
         (take (* n n)) ; Make sure 0 is empty
         (map-indexed list)
         position-value-list->matrix
         (map #(map (comp inc first) %))))

  (spiral 3)

  ) ;; End of rich comment block
