(ns helloworld.jop-chap7-functional-programming)

; vectors maps etc. are a function of their indices
(= :a ([:a :b] 0))
(= 1 ({ :a 1 :b 2 } :a))

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

