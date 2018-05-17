(ns helloworld.jop-chap7-functional-programming)

; vectors maps etc. are a function of their indices
(= :a ([:a :b] 0))
(= 1 ({:a 1 :b 2} :a))

; this can be used to greater effect by passing the vec as a fn arg
(= [:harry :luna]
   (map [:harry :ron :hermione :luna] #{0 3})
   (map [:harry :ron :heromione :luna] [0 3]))

; creating functions within other functions 
(def fifth (comp first rest rest rest rest)) ; read right to left
(= 5 (fifth [1 2 3 4 5]))

(def fifth (comp first rest rest rest rest)) ; read left to right
(= 5 (fifth [1 2 3 4 5]))

(defn fnth [n]
  (apply comp
         (cons first
               (take (dec n) (repeat rest)))))

((fnth 3) [1 2 3 4 5 5])

;; SHARING CLOSURE CONTEXT

(def bearings [{:x 0, :y 1}    ; north
               {:x 1, :y 0}    ; east 
               {:x 0, :y -1}   ; south
               {:x -1, :y 0}]) ; west

(defn forward [x y bearing-num]
  [(+ x (:x (bearings bearing-num)))
   (+ y (:y (bearings bearing-num)))])

(forward 5 5 0) ; move one step along bearings[0] (north)
(forward 5 5 1) ; move one step along bearings[1] (south)
(forward 5 5 2) ; move one step along bearings[2] (east)
(forward 5 5 3) ; move one step along bearings[3] (west)

(defn bot [x y bearing-num]
  {:coords [x y]
   :bearing ([:north :east :south :west] bearing-num)
   :forward (fn [] (bot (+ x (:x (bearings bearing-num)))
                        (+ y (:y (bearings bearing-num)))
                        bearing-num))
   :turn-right (fn [] (bot x y (mod (+ 1 bearing-num) 4)))
   :turn-left  (fn [] (bot x y (mod (- 1 bearing-num) 4)))})

(:coords (bot 5 5 0))
(:bearing (bot 5 5 0))
;; (:forward (bot 5 5 0))

(:coords ((:forward (bot 5 5 0))))

(:bearing ((:forward ((:forward ((:turn-right (bot 5 5 0))))))))
(:coords ((:forward ((:forward ((:turn-right (bot 5 5 0))))))))

;; THINKING RECURSIVELY

;; mundane recursion
(defn pow [base exp]
  (if (zero? exp)
    1
    (* base (pow base (dec exp)))))

(pow 2 2)
(pow 2 3)
(pow 2 20)
(pow 2 1000)

; fix the stack overflow....

(defn pow [base exp]
  (letfn [(kapow [base exp acc]
            (if (zero? exp)
              acc
              (recur base (dec exp) (* base acc))))]
    (kapow base exp 1)))

(pow 2N 1000)

;; A RECURSIVE UNIT CONVERSION CALCULATOR

(def simple-metric {:meter 1,
                    :km 1000,
                    :cm 1/100
                    :mm [1/10 :cm]})

(* 3 (:km simple-metric))

;; how many meters are in 3km, 10m, 80cm and 10mm combined?
(-> (*     3 (:km simple-metric))
    (+ (* 10 (:meter simple-metric)))
    (+ (* 80 (:cm simple-metric)))
    (+ (* (:cm simple-metric)
          (* 10 (first (:mm simple-metric)))))
    float)

;; can we write a generic function to convert units?

(def simple-metric {:meter 1
                    :km 1000
                    :cm 1/100
                    :mm [1/10 :cm]})

(def simple-imperial {:inch 1
                      :foot 12
                      :yard [3 :foot]
                      :furlong [220 :yard]
                      :mile [1760 :yard]
                      :marathon [26.22 :mile]})

(defn convert [context descriptor]
  (reduce (fn [result [mag unit]]
            (+ result
               (let [val (get context unit)]
                 (if (vector? val)
                   (* mag (convert context val))
                   (* mag val)))))
          0
          (partition 2 descriptor)))

(float (convert simple-metric [1 :km 110 :meter 10 :cm 1000 :mm]))
(convert {:bit 1 :byte 8 :nibble [1/2 :byte]} [32 :nibble])

(partition 2 [32 :nibble])
(partition 2 [1 :km 110 :meter])

(convert simple-imperial [1 :yard])

