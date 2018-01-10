(ns chainring-service.middleware-test
  (:require [clojure.test :refer :all]
            [chainring-service.middleware :refer :all]))

;
; Common functions used by tests.
;

(defn callable?
    "Test if given function-name is bound to the real function."
    [function-name]
    (clojure.test/function? function-name))

;
; Tests for various functions existence
;

(deftest test-inject-configuration-existence
    "Check that the chainring-service.middleware/inject-configuration definition exists."
    (testing "if the chainring-service.middleware/inject-configuration definition exists."
        (is (callable? 'chainring-service.middleware/inject-configuration))))

;
; Tests for function behaviours.
;

(deftest test-inject-configuration-1
    "Check the behaviour of function chainring-service.middleware/inject-configuration."
    (testing "The function chainring-service.middleware/inject-configuration."
        (let [function (inject-configuration (fn [x] x) :cfg)]
            (are [x y] (= x y)
                {:configuration :cfg} (function nil)
                {:configuration :cfg} (function {})
                {:configuration :cfg :foo :bar} (function {:foo :bar})))))

(deftest test-inject-configuration-2
    "Check the behaviour of function chainring-service.middleware/inject-configuration."
    (testing "The function chainring-service.middleware/inject-configuration."
        (let [function (inject-configuration (fn [x] nil) :cfg)]
            (are [x y] (= x y)
                nil (function nil)
                nil (function {})
                nil (function {:foo :bar})
                nil (function {:foo :bar :baz :bar})))))

(deftest test-inject-configuration-not-NPE
    "Check the behaviour of function chainring-service.middleware/inject-configuration."
    (testing "The function chainring-service.middleware/inject-configuration."
        (let [function (inject-configuration (fn [x] x) nil)]
            (are [x y] (= x y)
                {:configuration nil} (function nil)
                {:configuration nil} (function {})
                {:configuration nil :foo :bar} (function {:foo :bar})))))

