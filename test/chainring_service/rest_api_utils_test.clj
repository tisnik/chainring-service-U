(ns chainring-service.rest-api-utils-test
  (:require [clojure.test :refer :all]
            [chainring-service.rest-api-utils :refer :all]))

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

(deftest test-read-request-body-existence
    "Check that the chainring-service.rest-api-utils/read-request-body definition exists."
    (testing "if the chainring-service.rest-api-utils/read-request-body definition exists."
        (is (callable? 'chainring-service.rest-api-utils/read-request-body))))


(deftest test-body->results-existence
    "Check that the chainring-service.rest-api-utils/body->results definition exists."
    (testing "if the chainring-service.rest-api-utils/body->results definition exists."
        (is (callable? 'chainring-service.rest-api-utils/body->results))))


(deftest test-send-response-existence
    "Check that the chainring-service.rest-api-utils/send-response definition exists."
    (testing "if the chainring-service.rest-api-utils/send-response definition exists."
        (is (callable? 'chainring-service.rest-api-utils/send-response))))


(deftest test-send-ok-response-existence
    "Check that the chainring-service.rest-api-utils/send-ok-response definition exists."
    (testing "if the chainring-service.rest-api-utils/send-ok-response definition exists."
        (is (callable? 'chainring-service.rest-api-utils/send-ok-response))))


(deftest test-send-error-response-existence
    "Check that the chainring-service.rest-api-utils/send-error-response definition exists."
    (testing "if the chainring-service.rest-api-utils/send-error-response definition exists."
        (is (callable? 'chainring-service.rest-api-utils/send-error-response))))


(deftest test-send-plain-response-existence
    "Check that the chainring-service.rest-api-utils/send-plain-response definition exists."
    (testing "if the chainring-service.rest-api-utils/send-plain-response definition exists."
        (is (callable? 'chainring-service.rest-api-utils/send-plain-response))))


