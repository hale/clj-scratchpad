(ns helloworld.jop-a-star-pathfinding)

(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
                        size
                        yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %))
                deltas))))

(neighbors 5 [0 0]) ; ([1 0] [0 1])
(neighbors 5 [0 1]) ; ([1 1] [0 0] [0 2])
(neighbors 5 [1 1]) ; ([0 1] [2 1] [1 0] [1 2])

(map (fn [x] (* x x)) [1 2 3 4 5 ]) ; (1 4 9 16 25)
(map + [1 2] [3 4] [5 6])  ; (9 12)
(map + [1 2 3] [4 5 6]) ; (5 7 9)

(def world [[  1   1   1   1   1]
            [999 999 999 999   1]
            [  1   1   1   1   1]
            [  1 999 999 999 999]
            [  1   1   1   1   1]])

;; h(), the heuristic function
;; (distance from start (all the way right and down) - progress - 2 because we're already on a square)
(defn estimate-cost [step-cost-est size y x]
  (* step-cost-est
     (- (+ size size) y x 2)))

(estimate-cost 1 5 0 0) ; 8
(estimate-cost 900 5 0 0) ; 7200
(estimate-cost 900 5 4 4) ; 0

;; g(), path so-far. nbr stands for 'neighbor'...
(defn path-cost [node-cost cheapest-nbr]
  (+ node-cost
     (or (:cost cheapest-nbr) 0)))

;; f(), the total cost function
(defn total-cost [newcost step-cost-est size y x]
  (+ newcost
     (estimate-cost step-cost-est size y x)))

(total-cost 0 900 5 0 0)
(total-cost 100 900 5 3 4)

;; putting it all together:
(total-cost (path-cost 900 {:cost 1}) 900 5 3 4)


;; function to retrieve the minimum value based on a criteria function
(defn min-by [f coll]
  (when (seq coll)
    (reduce (fn [min other]
              (if (> (f min) (f other))
                other
                min))
            coll)))

(min-by :cost [{:cost 100} {:cost 2} {:cost 36} {:cost 9}])
(min-by :cost []) ; enabled by the (when (seq coll))



;; main A* algorithm

(defn astar [start-yx step-est cell-costs]
  (let [size (count cell-costs)]
    (loop [steps 0
           routes (vec (replicate size (vec (replicate size nil))))
           work-todo (sorted-set [0 start-yx])]
      (if (empty? work-todo)
        [(peek (peek routes)) :steps steps]
        (let [[_ yx :as work-item] (first work-todo)
              rest-work-todo (disj work-todo work-item)
              nbr-yxs (neighbors size yx)
              cheapest-nbr (min-by :cost
                                   (keep #(get-in routes %)
                                         nbr-yxs))
              newcost (path-cost (get-in cell-costs yx)
                                 cheapest-nbr)
              oldcost (:cost (get-in routes yx))]
          (if (and oldcost (>= newcost oldcost))
            (recur (inc steps) routes rest-work-todo)
            (recur (inc steps)
                   (assoc-in routes yx
                             (:cost newcost
                                    :yxs (conj (:yxs cheapest-nbr [])
                                               yx)))
                   (into rest-work-todo
                         (map
                          (fn [w]
                            (let [[y x] w]
                              [(total-cost newcost step-est size y x) w]))
                          nbr-yxs)))))))))


(astar [0 0] 900 world)
















