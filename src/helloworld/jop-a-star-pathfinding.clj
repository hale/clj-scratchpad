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

(def world [[  1   1   1   1   1]
            [999 999 999 999   1]
            [  1   1   1   1   1]
            [  1 999 999 999 999]
            [  1   1   1   1   1]])

