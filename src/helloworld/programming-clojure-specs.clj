(ns helloworld.programming-clojure-specs
  (:require [clojure.spec.alpha :as s]))


(s/valid? number? 44)
