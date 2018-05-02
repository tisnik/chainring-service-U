(ns chainring-service.config-test
  (:require [clojure.test :refer :all]
            [chainring-service.html-renderer :refer :all]))

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


(deftest test-full-prefix-existence
    "Check that the chainring-service.config/full-prefix definition exists."
    (testing "if the chainring-service.config/full-prefix definition exists."
        (is (callable? 'chainring-service.config/full-prefix))))


(deftest test-update-configuration-existence
    "Check that the chainring-service.config/update-configuration definition exists."
    (testing "if the chainring-service.config/update-configuration definition exists."
        (is (callable? 'chainring-service.config/update-configuration))))


(deftest test-load-configuration-from-ini-existence
    "Check that the chainring-service.config/load-configuration-from-ini definition exists."
    (testing "if the chainring-service.config/load-configuration-from-ini definition exists."
        (is (callable? 'chainring-service.config/load-configuration-from-ini))))


(deftest test-pretty-print?-existence
    "Check that the chainring-service.config/pretty-print? definition exists."
    (testing "if the chainring-service.config/pretty-print? definition exists."
        (is (callable? 'chainring-service.config/pretty-print?))))


(deftest test-get-api-prefix-existence
    "Check that the chainring-service.config/get-api-prefix definition exists."
    (testing "if the chainring-service.config/get-api-prefix definition exists."
        (is (callable? 'chainring-service.config/get-api-prefix))))


(deftest test-get-api-version-existence
    "Check that the chainring-service.config/get-api-version definition exists."
    (testing "if the chainring-service.config/get-api-version definition exists."
        (is (callable? 'chainring-service.config/get-api-version))))


(deftest test-get-version-existence
    "Check that the chainring-service.config/get-version definition exists."
    (testing "if the chainring-service.config/get-version definition exists."
        (is (callable? 'chainring-service.config/get-version))))


(deftest test-get-db-version-existence
    "Check that the chainring-service.config/get-db-version definition exists."
    (testing "if the chainring-service.config/get-db-version definition exists."
        (is (callable? 'chainring-service.config/get-db-version))))


(deftest test-get-api-full-prefix-existence
    "Check that the chainring-service.config/get-api-full-prefix definition exists."
    (testing "if the chainring-service.config/get-api-full-prefix definition exists."
        (is (callable? 'chainring-service.config/get-api-full-prefix))))


(deftest test-verbose?-existence
    "Check that the chainring-service.config/verbose? definition exists."
    (testing "if the chainring-service.config/verbose? definition exists."
        (is (callable? 'chainring-service.config/verbose?))))

