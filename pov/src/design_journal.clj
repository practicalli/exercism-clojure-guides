(ns design-journal)

(comment

  (defn look-up [hidden graph]
    (let [exists? (partial some) findHidden (partial = hidden)
          vect    (comp (partial apply vector) (partial filter vector?))
          nVect   (comp (partial apply vector) (partial filter (complement vector?)))]

      (if (not (exists? findHidden (flatten graph))) nil

          (if (exists? findHidden graph)
            [(into [hidden] (remove findHidden graph))]

            (as-> graph g (vect g)
                  (filter (comp (partial exists? findHidden) flatten) g)
                  (reduce into g)
                  (look-up hidden g)
                  (conj g (->> graph
                               vect
                               (remove (comp (partial exists? findHidden) flatten))
                               (apply conj (nVect graph)))))))))

  (defn of [hidden graph]
    (let [graph (look-up hidden graph)]
      (if (= graph nil) nil
          (->> graph
               reverse
               (reduce #(conj %2 %1))))))

  (defn locate-path [end graph]
    (let [exists? (partial some (partial = end))]
      (cond
        (exists? graph)                         end
        (-> graph ((comp exists? flatten)) not) nil
        :else                                   (->> graph (filter vector?)
                                                     (filter (comp exists? flatten))
                                                     (reduce into)
                                                     (locate-path end)
                                                     (conj [(first graph)])))))

  (defn path-from-to [start end graph]
    (let [graph (of start graph)
          route (locate-path end graph)]
      (if (= route nil) nil
          (->> route flatten (apply vector))))))
