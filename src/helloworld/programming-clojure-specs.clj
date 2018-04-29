(ns helloworld.programming-clojure-specs
  (:require [clojure.spec.alpha :as s]))


(clojure.spec/valid? number? 44)
