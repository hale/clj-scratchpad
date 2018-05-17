(def answers {:square-ft 100
              :construction "New Construction"
              :type-of-work ["Grading"]})

(def archeological-review-fee
  {:name "Archeological Review"
   :processing-time 30
   :conditions ['(= (:construction answers) "New Construction")
                '(contains? (:type-of-work answers) "Demolition")]
   :calculation '((170))})

(def building-permit {:fees [archeological-review-fee]})

(defn active-fees [permit, answers]
  "Run all conditions to see if fee is active"
  (filter (fn [fee] (every? (fn [condition] (condition)) (:conditions fee))) (:fees permit)))


(defn total-cost [fees]
  (reduce + (map :calculation fees))) 

(active-fees building-permit answers)
(total-cost (active-fees building-permit answers))

(map :calculation (:fees building-permit))

(:fees building-permit)





;; DSL?
;; (:construction is "New Construction" or :type-of-work includes "Demolition")
