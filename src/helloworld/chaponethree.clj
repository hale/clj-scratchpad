(ns helloworld.chaponethree)

; Funciton definition
(def say-hello "hello")
(println say-hello)

; Function definition with args
(defn hello [name] (println (str "Hello, " name)))
(hello "Phil")

; Destructure with a vector
(defn hello-vector [full-name]
  (let [[f-name m-name l-name & more] full-name
        s (str l-name ", " f-name " " m-name "  more" more)]
    (println s)))
(hello-vector ["Philip" "Gordon" "Hale"])
(hello-vector ["Philip" "Gordon" "Hale" "PG Hale Ltd."])

; Destructure with a map
(defn hello-map [person]
  (let [{f-name :f-name m-name :m-name l-name :l-name} person
        s (str l-name ", " f-name " " m-name)]
    (println s)))
(hello-map {:f-name "Phil" :m-name "Gordon" :l-name "Hale"})

