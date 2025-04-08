Thank you for submitting this solution.

This is a good solution to the challenge and counting strings is a commonly submitted approach.

It is possible to count the number of digits in the number using math.  The `quot`, `rem` and `mod` functions are common core functions for working with numbers.

If `quot` of a number to base 10 is iterated over the number, then each digit of the number can be returned.  Be careful if using a function like iterate as this can generate an infinite sequence (blowing up the heap), if some context is not given.

The submitted solution has many nested expressions, which can make the code a little harder to parse for the human.  One approach to this is to tweak the formatting (hopefully that shows up okay on exercism),

```
  (defn armstrong? [num]
    (== num
        (reduce + 0
                (map
                  #(reduce * (repeat (count (str num))
                                     (Character/digit % 10)))
                  (str num)))))

```

Another approach is to use the threading macros
[https://clojure.org/guides/threading_macros](https://clojure.org/guides/threading_macros)

This syntax is very useful, especially when showing a sequence of processing steps / transformations
There are some examples of using threading macros here
[https://github.com/practicalli/clojure-through-code/blob/master/src/clojure_through_code/hhgttg-book-common-words.clj](https://github.com/practicalli/clojure-through-code/blob/master/src/clojure_through_code/hhgttg-book-common-words.clj)
