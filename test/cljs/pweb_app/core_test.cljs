(ns pweb-app.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [pjstadig.humane-test-output]
            [pweb-app.core :as rc]))

(deftest test-home
  (is (= true true)))

