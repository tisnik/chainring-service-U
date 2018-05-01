(ns chainring-service.server-test
  (:require [clojure.test :refer :all]
            [chainring-service.server :refer :all]))

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

(deftest test-finish-processing-existence
    "Check that the chainring-service.server/finish-processing definition exists."
    (testing "if the chainring-service.server/finish-processing definition exists."
        (is (callable? 'chainring-service.server/finish-processing))))


(deftest test-process-front-page-existence
    "Check that the chainring-service.server/process-front-page definition exists."
    (testing "if the chainring-service.server/process-front-page definition exists."
        (is (callable? 'chainring-service.server/process-front-page))))


(deftest test-process-project-list-page-existence
    "Check that the chainring-service.server/process-project-list-page definition exists."
    (testing "if the chainring-service.server/process-project-list-page definition exists."
        (is (callable? 'chainring-service.server/process-project-list-page))))


(deftest test-process-project-page-existence
    "Check that the chainring-service.server/process-project-page definition exists."
    (testing "if the chainring-service.server/process-project-page definition exists."
        (is (callable? 'chainring-service.server/process-project-page))))


(deftest test-process-building-page-existence
    "Check that the chainring-service.server/process-building-page definition exists."
    (testing "if the chainring-service.server/process-building-page definition exists."
        (is (callable? 'chainring-service.server/process-building-page))))


(deftest test-process-drawing-page-existence
    "Check that the chainring-service.server/process-drawing-page definition exists."
    (testing "if the chainring-service.server/process-drawing-page definition exists."
        (is (callable? 'chainring-service.server/process-drawing-page))))


(deftest test-api-call-handler-existence
    "Check that the chainring-service.server/api-call-handler definition exists."
    (testing "if the chainring-service.server/api-call-handler definition exists."
        (is (callable? 'chainring-service.server/api-call-handler))))


(deftest test-gui-call-handler-existence
    "Check that the chainring-service.server/gui-call-handler definition exists."
    (testing "if the chainring-service.server/gui-call-handler definition exists."
        (is (callable? 'chainring-service.server/gui-call-handler))))


(deftest test-handler-existence
    "Check that the chainring-service.server/handler definition exists."
    (testing "if the chainring-service.server/handler definition exists."
        (is (callable? 'chainring-service.server/handler))))

