(ns chainring-service.vector-drawing-test
  (:require [clojure.test :refer :all]
            [chainring-service.vector-drawing :refer :all]))

;
; Common functions used by tests.
;

(defn callable?
    "Test if given function-name is bound to the real function."
    [function-name]
    (clojure.test/function? function-name))

;
; Tests for functions existence
;

(deftest test-send-drawing-existence
    "Check that the chainring-service.vector-drawing/send-drawing definition exists."
    (testing "if the chainring-service.vector-drawing/send-drawing definition exists."
        (is (callable? 'chainring-service.vector-drawing/send-drawing))))


(deftest test-vector-drawing-as-drw-existence
    "Check that the chainring-service.vector-drawing/vector-drawing-as-drw definition exists."
    (testing "if the chainring-service.vector-drawing/vector-drawing-as-drw definition exists."
        (is (callable? 'chainring-service.vector-drawing/vector-drawing-as-drw))))


(deftest test-vector-drawing-as-json-existence
    "Check that the chainring-service.vector-drawing/vector-drawing-as-json definition exists."
    (testing "if the chainring-service.vector-drawing/vector-drawing-as-json definition exists."
        (is (callable? 'chainring-service.vector-drawing/vector-drawing-as-json))))


(deftest test-vector-drawing-as-binary-existence
    "Check that the chainring-service.vector-drawing/vector-drawing-as-binary definition exists."
    (testing "if the chainring-service.vector-drawing/vector-drawing-as-binary definition exists."
        (is (callable? 'chainring-service.vector-drawing/vector-drawing-as-binary))))

