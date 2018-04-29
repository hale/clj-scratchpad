(ns helloworld.transducers)

; http://elbenshira.com/blog/understanding-transducers/


;;; 1. Filter and Map

; (doc map)
; (doc filter)

; 0-10 shifted to the right on the number line 
(map inc (range 10))

; even numbers between 1 and 10 inclusive
(filter even? '(1 2 3 4 5 6 7 8 9 10))
; same, with nested fns
(filter even? (map inc (range 10)))

;;; 2. map can be written as reduce

;(doc reduce)
(reduce + [1 2 3 4])
(reduce (fn [x y] (+ x y)) [1 2 3 4])

;(doc conj)
; Takes: an initial vector and a value
; Returns: appends the incremented value to the vector
(defn map-inc-reducer
  [result input]
  (conj result (inc input)))

(map-inc-reducer [] 1)
(map-inc-reducer [1] 9)

; When reduce is called with three arguments, the second 'val' is used as the
; initial arg for the reducing function. Otherwise, the initial arg is the first
; element of the collection.

; So what this does is calls the reducing function 'map-inc-reducer' in a loop,
; one for each value in the collection (plus once more to start with, using the
; initial value [])
(reduce map-inc-reducer [] (range 10))

; write as a higher order function so we can pass `inc`

(defn map-reducer
  [f]
  (fn [result input]
    (conj result (f input))))

(reduce (map-reducer inc) [] (range 10))
(reduce (map-reducer dec) [] (range 10))
(reduce (map-reducer #(* % %)) [] (range 10))


; Let's do the same with filter

(filter even? '(1 2 3 4 5 6 7 8 9 10))

(defn filter-even-reducer
  [result input]
  (if (even? input)
    (conj result input)
    result))

(reduce filter-even-reducer [] '(1 2 3 4 5 6 7 8 9 10))

(defn filter-reducer
  [predicate]
  (fn [result input]
    (if (predicate input)
      (conj result input)
      result)))

(reduce (filter-reducer even?) [] '(1 2 3 4 5 6 7 8 9 10))

(reduce (filter-reducer odd?) [] (range 10))

;;; 3. Compose the map-reducer and filter-reducer!

(reduce (filter-reducer even?) [] (reduce (map-reducer inc) [] (range 10)))

; the above is the same as:
(filter even? (map inc (range 10)))

; We see that with higher-order functions, we are able to define map and filter
; in terms of reduce.

; Both versions, however, required intermediate vectors—one for map and one for
; filter. One important property of transducers is that they should employ only
; one collection regardless of the number of transformations. How can we
; accomplish that?


(defn mapping
  [f]
  (fn [reducing]
    (fn [result input]
      (reducing result (f input)))))

(defn filtering
  [predicate]
  (fn [reducing]
    (fn [result input]
      (if (predicate input)
        (reducing result input)
        result))))

(reduce
 ((filtering even?) conj)
 []
 (reduce
  ((mapping inc) conj)
  []
  (range 10)))


(((mapping inc) conj) [] 1)
(((mapping inc) conj) [2] 2)

((mapping inc) ((filtering even?) conj))

(reduce ((mapping inc) ((filtering even?) conj)) [] (range 10))

(def xform
  (comp
   (mapping inc)
   (filtering even?)))

(reduce (xform conj) [] (range 10))

(defn square [x] (* x x))

(def xform
  (comp
   (filtering even?)
   (filtering #(< % 10))
   (mapping square)
   (mapping inc)))

(reduce (xform conj) [] (range 10))


;; EXERCISES

;; Given a string, use transducers to:
;; 
;; 1. Filter out vowels and non-ASCII characters
;; 2. Filter out upper-case characters
;; 3. Rotate all remaining characters via a Caesar cipher,
;; 4. And reduce the rotated characters into a map counting the number of
;;    occurrences of each character.
;; 

(def vowels #{\a \e \i \o \u})

(defn consonant? [c] (= false (contains? vowels c)))

(re-seq #​"\w+"​ "abcd123")

(re-seq #​"\w+"​ ​"the quick brown fox")

(re-seq #"\W+" "THE quick brown fox")

(filter #() )

(defn lowercase? [c]
  (re-matches "[abc]" (seq c)))

(defn caesar-count [string, cypher]
  (reduce
   (comp ((filtering consonant?) str)
      ((filtering lowercase?) str)
   ) "" string))


(caesar-count "abcdefghi" 0)

