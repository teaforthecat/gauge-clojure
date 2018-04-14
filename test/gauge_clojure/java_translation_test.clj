(ns gauge-clojure.java-translation-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.java.data :refer [from-java]]
            [gauge-clojure.java-translation :as jt]))






(deftest walk-from-java
  (testing "ExecuteStep (there and back again)"
    (let [result (jt/walk-from-java
                  (jt/proto-message :execute-step
                                    :execute-step-request
                                    :actual-step-text "abc"))]
      (is (= "abc" (get-in result [:execute-step-request :actual-step-text]))))))
