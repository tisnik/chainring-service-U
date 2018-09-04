(ns chainring-service.rest-api-test
  (:require [clojure.test :refer :all]
            [chainring-service.rest-api :refer :all]))

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

(deftest test-unknown-endpoint-handler-existence
    "Check that the chainring-service.rest-api/unknown-endpoint-handler definition exists."
    (testing "if the chainring-service.rest-api/unknown-endpoint-handler definition exists."
        (is (callable? 'chainring-service.rest-api/unknown-endpoint-handler))))


(deftest test-toplevel-handler-existence
    "Check that the chainring-service.rest-api/toplevel-handler definition exists."
    (testing "if the chainring-service.rest-api/toplevel-handler definition exists."
        (is (callable? 'chainring-service.rest-api/toplevel-handler))))


(deftest test-api-info-handler-existence
    "Check that the chainring-service.rest-api/api-info-handler definition exists."
    (testing "if the chainring-service.rest-api/api-info-handler definition exists."
        (is (callable? 'chainring-service.rest-api/api-info-handler))))


(deftest test-info-handler-existence
    "Check that the chainring-service.rest-api/info-handler definition exists."
    (testing "if the chainring-service.rest-api/info-handler definition exists."
        (is (callable? 'chainring-service.rest-api/info-handler))))


(deftest test-liveness-handler-existence
    "Check that the chainring-service.rest-api/liveness-handler definition exists."
    (testing "if the chainring-service.rest-api/liveness-handler definition exists."
        (is (callable? 'chainring-service.rest-api/liveness-handler))))


(deftest test-readiness-handler-existence
    "Check that the chainring-service.rest-api/readiness-handler definition exists."
    (testing "if the chainring-service.rest-api/readiness-handler definition exists."
        (is (callable? 'chainring-service.rest-api/readiness-handler))))


(deftest test-config-handler-existence
    "Check that the chainring-service.rest-api/config-handler definition exists."
    (testing "if the chainring-service.rest-api/config-handler definition exists."
        (is (callable? 'chainring-service.rest-api/config-handler))))


(deftest test-project-list-handler-existence
    "Check that the chainring-service.rest-api/project-list-handler definition exists."
    (testing "if the chainring-service.rest-api/project-list-handler definition exists."
        (is (callable? 'chainring-service.rest-api/project-list-handler))))


(deftest test-read-project-info-existence
    "Check that the chainring-service.rest-api/read-project-info definition exists."
    (testing "if the chainring-service.rest-api/read-project-info definition exists."
        (is (callable? 'chainring-service.rest-api/read-project-info))))


(deftest test-read-building-info-existence
    "Check that the chainring-service.rest-api/read-building-info definition exists."
    (testing "if the chainring-service.rest-api/read-building-info definition exists."
        (is (callable? 'chainring-service.rest-api/read-building-info))))


(deftest test-project-handler-existence
    "Check that the chainring-service.rest-api/project-handler definition exists."
    (testing "if the chainring-service.rest-api/project-handler definition exists."
        (is (callable? 'chainring-service.rest-api/project-handler))))


(deftest test-building-handler-existence
    "Check that the chainring-service.rest-api/building-handler definition exists."
    (testing "if the chainring-service.rest-api/building-handler definition exists."
        (is (callable? 'chainring-service.rest-api/building-handler))))


(deftest test-floor-handler-existence
    "Check that the chainring-service.rest-api/floor-handler definition exists."
    (testing "if the chainring-service.rest-api/floor-handler definition exists."
        (is (callable? 'chainring-service.rest-api/floor-handler))))


(deftest test-drawing-handler-existence
    "Check that the chainring-service.rest-api/drawing-handler definition exists."
    (testing "if the chainring-service.rest-api/drawing-handler definition exists."
        (is (callable? 'chainring-service.rest-api/drawing-handler))))


(deftest test-all-projects-existence
    "Check that the chainring-service.rest-api/all-projects definition exists."
    (testing "if the chainring-service.rest-api/all-projects definition exists."
        (is (callable? 'chainring-service.rest-api/all-projects))))


(deftest test-all-buildings-existence
    "Check that the chainring-service.rest-api/all-buildings definition exists."
    (testing "if the chainring-service.rest-api/all-buildings definition exists."
        (is (callable? 'chainring-service.rest-api/all-buildings))))


(deftest test-all-floors-existence
    "Check that the chainring-service.rest-api/all-floors definition exists."
    (testing "if the chainring-service.rest-api/all-floors definition exists."
        (is (callable? 'chainring-service.rest-api/all-floors))))


(deftest test-all-drawings-handler-existence
    "Check that the chainring-service.rest-api/all-drawings-handler definition exists."
    (testing "if the chainring-service.rest-api/all-drawings-handler definition exists."
        (is (callable? 'chainring-service.rest-api/all-drawings-handler))))


(deftest test-drawings-cache-info-handler-existence
    "Check that the chainring-service.rest-api/drawings-cache-info-handler definition exists."
    (testing "if the chainring-service.rest-api/drawings-cache-info-handler definition exists."
        (is (callable? 'chainring-service.rest-api/drawings-cache-info-handler))))


(deftest test-store-drawing-raw-data-existence
    "Check that the chainring-service.rest-api/store-drawing-raw-data definition exists."
    (testing "if the chainring-service.rest-api/store-drawing-raw-data definition exists."
        (is (callable? 'chainring-service.rest-api/store-drawing-raw-data))))


(deftest test-missing-parameter-existence
    "Check that the chainring-service.rest-api/missing-parameter definition exists."
    (testing "if the chainring-service.rest-api/missing-parameter definition exists."
        (is (callable? 'chainring-service.rest-api/missing-parameter))))


(deftest test-try-to-load-drawing-existence
    "Check that the chainring-service.rest-api/try-to-load-drawing definition exists."
    (testing "if the chainring-service.rest-api/try-to-load-drawing definition exists."
        (is (callable? 'chainring-service.rest-api/try-to-load-drawing))))


(deftest test-deserialize-drawing-existence
    "Check that the chainring-service.rest-api/deserialize-drawing definition exists."
    (testing "if the chainring-service.rest-api/deserialize-drawing definition exists."
        (is (callable? 'chainring-service.rest-api/deserialize-drawing))))


(deftest test-try-to-store-drawing-existence
    "Check that the chainring-service.rest-api/try-to-store-drawing definition exists."
    (testing "if the chainring-service.rest-api/try-to-store-drawing definition exists."
        (is (callable? 'chainring-service.rest-api/try-to-store-drawing))))


(deftest test-serialize-drawing-existence
    "Check that the chainring-service.rest-api/serialize-drawing definition exists."
    (testing "if the chainring-service.rest-api/serialize-drawing definition exists."
        (is (callable? 'chainring-service.rest-api/serialize-drawing))))

