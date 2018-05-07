(ns helloworld.if-let)

(def anonymous-book {:title "Sir Gawain and the Green Knight"} )

(def with-author {:title "Once and Future King" :author "White"} )

;; naive implementation
(defn uppercase-author
  "uppercase the author name, or nil if no author"
  [book]
  (if (:author book) (clojure.string/upper-case (:author book)) nil ))
(uppercase-author anonymous-book)
(uppercase-author with-author)

;; refactor to use let
(defn uppercase-author
  "uppercase the author name, or nil if no author"
  [book]
  (let [author (:author book)]
    (if author (clojure.string/upper-case author) nil )))
(uppercase-author anonymous-book)
(uppercase-author with-author)

;; refactor to use destructuring
(defn uppercase-author
  "uppercase the author name, or nil if no author"
  [{author :author}]
  (if author (clojure.string/upper-case author))))
(uppercase-author anonymous-book)
(uppercase-author with-author)

;; refactor to use if-let macro
(defn uppercase-author
  "uppercase the author name, or nil if no author"
  [book]
  (if-let [author (:author book)]
    (clojure.string/upper-case author)))
(uppercase-author anonymous-book)
(uppercase-author with-author)
