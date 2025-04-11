;; ---------------------------------------------------------
    ;; Hamming Design Journal
    ;;
    ;; Exploring in the REPL to find valuable solutions
;; ---------------------------------------------------------


;; ---------------------------------------------------------
(ns mentor-journal)
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
(comment

  ;; https://exercism.io/mentor/solutions/07dd2de5c88a42fc89eae408f9d3f01e?iteration_idx=1

  ;; Mentor comments
  ;; instead of mapping the strings to a sequence of 0s and 1s and adding them, mapping = and then counting the false?s is more readable.

  ;; Also, I'd rather write same-size? inline, instead of as a separate function.

  ;; These are of course, my own subjective preferences.

  ;; Looks like this:

  ;; (defn distance [s1 s2]
  ;;   (when (= (count s1) (count s2))
  ;;     (count
  ;;       (filter false?
  ;;               (map = s1 s2)))))

  ;; And to make this read more linearly (top-to-bottom), we can use the thread-last macro (->>). Looks like this:

  ;; (defn distance [s1 s2]
  ;;   (when (= (count s1) (count s2))
  ;;     (->> (map = s1 s2)
  ;;          (filter false?)
  ;;          (count))))
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Submission 1

  (defn- same-size? [xs ys]
    (= (count xs) (count ys)))

  (defn- zero-one-distance [x y]
    (if (= x y) 0 1))

  (defn distance [xs ys]
    (when (same-size? xs ys)
      (apply + (map zero-one-distance xs ys))))
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Submission 2

  (defn distance [xs ys]
    (when (= (count xs) (count ys))
      (->> (map = xs ys)
           (filter false?)
           (count)))))

;; End of rich comment block
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Rich comment block with redefined vars ignored
(comment

  (ns mentor-journal)

  (defn distance [xs ys]
    (when (= (count xs) (count ys))
      (->> (map = xs ys)
           (filter false?)
           (count)))))

   ;; End of rich comment block
;; ---------------------------------------------------------
