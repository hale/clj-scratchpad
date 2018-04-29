(ns helloworld.graphics)

(defn f-values [f max-x max-y]
  (for [x (range max-x) y (range max-y)]
    [x y (rem (f x y) 256)]))

(defn xors [max-x max-y] (f-values bit-xor max-x max-y))

(xors 200 200)

(def frame (java.awt.Frame.))
frame

;(for [meth (.getMethods java.awt.Frame)
;      :let [name (.getName meth)]
;      :when (re-find #"Vis" name)]
;  name)

(.setVisible frame true)
(.setSize frame (java.awt.Dimension. 200 200))
(def gfx (.getGraphics frame))

;(javadoc frame)

; (.fillRect gfx 100 100 50 75)
; (.setColor gfx (java.awt.Color. 255 128 0))
; (.fillRect gfx 100 150 75 50)

(defn clear [g] (.clearRect g 0 0 200 200))
(clear gfx)

(defn draw-values [f max-x max-y]
  (clear gfx)
  (.setSize frame (java.awt.Dimension. max-x max-y))
  (doseq [[x y v] (f-values f max-x max-y)]
    (.setColor gfx (java.awt.Color. v v v))
    (.fillRect gfx x y 1 1)))

(draw-values bit-and 256 256)
(draw-values bit-or 256 256)
(draw-values bit-xor 256 256)
(draw-values + 256 256)
(draw-values * 256 256)
(draw-values min 256 256)
(draw-values max 256 256)
