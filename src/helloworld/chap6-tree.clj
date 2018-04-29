(ns helloworld.chap3)

{:val 5 :L nil :R nil}

;; (doc cond)
;;
;; (:val nil)
;; ({} :val)
;; 
;; (:val {:val 3})
;; ({:val 3} :val)

;; Inserts the value into the tree. 
(defn xconj [t v]
  (cond
    (nil? t)       { :val v, :L nil, :R nil }
    (< v (:val t)) { :val (:val t),
                     :L (xconj (:L t) v),
                     :R (:R t) }
    :else          { :val (:val t)
                     :L (:L t)
                     :R (xconj (:R t) v) }))


(xconj nil 3)
(xconj nil 5)
(xconj (xconj nil 5) 3)
(xconj (xconj (xconj nil 8) 5) 3)

(def tree-a (xconj (xconj (xconj nil 3) 2) 1))
(def tree-b (xconj (xconj (xconj nil 1) 2) 3))
(def tree-c (xconj (xconj (xconj nil 3) 1) 2))

(defn xseq [t]
  (when t
    (concat (xseq (:l t)) [(:val t)] (xseq (:R t)))))


(xseq tree-a)
(xseq tree-b)
(xseq tree-c)
