(ns chainring-service.db-interface-test
  (:require [clojure.test :refer :all]
            [chainring-service.db-interface :refer :all]))

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

(deftest test-simple-query-sequence-existence
    "Check that the chainring-service.db-interface/simple-query-sequence definition exists."
    (testing "if the chainring-service.db-interface/simple-query-sequence definition exists."
        (is (callable? 'chainring-service.db-interface/simple-query-sequence))))


(deftest test-simple-query-existence
    "Check that the chainring-service.db-interface/simple-query definition exists."
    (testing "if the chainring-service.db-interface/simple-query definition exists."
        (is (callable? 'chainring-service.db-interface/simple-query))))


(deftest test-simple-query-selector-existence
    "Check that the chainring-service.db-interface/simple-query-selector definition exists."
    (testing "if the chainring-service.db-interface/simple-query-selector definition exists."
        (is (callable? 'chainring-service.db-interface/simple-query-selector))))


(deftest test-read-project-list-existence
    "Check that the chainring-service.db-interface/read-project-list definition exists."
    (testing "if the chainring-service.db-interface/read-project-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-project-list))))


(deftest test-read-project-name-existence
    "Check that the chainring-service.db-interface/read-project-name definition exists."
    (testing "if the chainring-service.db-interface/read-project-name definition exists."
        (is (callable? 'chainring-service.db-interface/read-project-name))))


(deftest test-read-project-info-existence
    "Check that the chainring-service.db-interface/read-project-info definition exists."
    (testing "if the chainring-service.db-interface/read-project-info definition exists."
        (is (callable? 'chainring-service.db-interface/read-project-info))))


(deftest test-read-detailed-project-info-existence
    "Check that the chainring-service.db-interface/read-detailed-project-info definition exists."
    (testing "if the chainring-service.db-interface/read-detailed-project-info definition exists."
        (is (callable? 'chainring-service.db-interface/read-detailed-project-info))))


(deftest test-read-all-buildings-existence
    "Check that the chainring-service.db-interface/read-all-buildings definition exists."
    (testing "if the chainring-service.db-interface/read-all-buildings definition exists."
        (is (callable? 'chainring-service.db-interface/read-all-buildings))))


(deftest test-read-building-list-existence
    "Check that the chainring-service.db-interface/read-building-list definition exists."
    (testing "if the chainring-service.db-interface/read-building-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-building-list))))


(deftest test-read-building-count-for-project-existence
    "Check that the chainring-service.db-interface/read-building-count-for-project definition exists."
    (testing "if the chainring-service.db-interface/read-building-count-for-project definition exists."
        (is (callable? 'chainring-service.db-interface/read-building-count-for-project))))


(deftest test-read-building-info-existence
    "Check that the chainring-service.db-interface/read-building-info definition exists."
    (testing "if the chainring-service.db-interface/read-building-info definition exists."
        (is (callable? 'chainring-service.db-interface/read-building-info))))


(deftest test-read-floor-count-for-building-existence
    "Check that the chainring-service.db-interface/read-floor-count-for-building definition exists."
    (testing "if the chainring-service.db-interface/read-floor-count-for-building definition exists."
        (is (callable? 'chainring-service.db-interface/read-floor-count-for-building))))


(deftest test-read-drawing-count-for-floor-existence
    "Check that the chainring-service.db-interface/read-drawing-count-for-floor definition exists."
    (testing "if the chainring-service.db-interface/read-drawing-count-for-floor definition exists."
        (is (callable? 'chainring-service.db-interface/read-drawing-count-for-floor))))


(deftest test-read-all-floors-existence
    "Check that the chainring-service.db-interface/read-all-floors definition exists."
    (testing "if the chainring-service.db-interface/read-all-floors definition exists."
        (is (callable? 'chainring-service.db-interface/read-all-floors))))


(deftest test-read-floor-list-existence
    "Check that the chainring-service.db-interface/read-floor-list definition exists."
    (testing "if the chainring-service.db-interface/read-floor-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-floor-list))))


(deftest test-read-floor-info-existence
    "Check that the chainring-service.db-interface/read-floor-info definition exists."
    (testing "if the chainring-service.db-interface/read-floor-info definition exists."
        (is (callable? 'chainring-service.db-interface/read-floor-info))))


(deftest test-read-all-drawings-existence
    "Check that the chainring-service.db-interface/read-all-drawings definition exists."
    (testing "if the chainring-service.db-interface/read-all-drawings definition exists."
        (is (callable? 'chainring-service.db-interface/read-all-drawings))))


(deftest test-read-drawing-list-existence
    "Check that the chainring-service.db-interface/read-drawing-list definition exists."
    (testing "if the chainring-service.db-interface/read-drawing-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-drawing-list))))


(deftest test-read-drawing-info-existence
    "Check that the chainring-service.db-interface/read-drawing-info definition exists."
    (testing "if the chainring-service.db-interface/read-drawing-info definition exists."
        (is (callable? 'chainring-service.db-interface/read-drawing-info))))


(deftest test-read-room-list-existence
    "Check that the chainring-service.db-interface/read-room-list definition exists."
    (testing "if the chainring-service.db-interface/read-room-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-room-list))))


(deftest test-read-sap-room-list-existence
    "Check that the chainring-service.db-interface/read-sap-room-list definition exists."
    (testing "if the chainring-service.db-interface/read-sap-room-list definition exists."
        (is (callable? 'chainring-service.db-interface/read-sap-room-list))))


(deftest test-read-sap-room-count-existence
    "Check that the chainring-service.db-interface/read-sap-room-count definition exists."
    (testing "if the chainring-service.db-interface/read-sap-room-count definition exists."
        (is (callable? 'chainring-service.db-interface/read-sap-room-count))))


(deftest test-store-drawing-raw-data-existence
    "Check that the chainring-service.db-interface/store-drawing-raw-data definition exists."
    (testing "if the chainring-service.db-interface/store-drawing-raw-data definition exists."
        (is (callable? 'chainring-service.db-interface/store-drawing-raw-data))))


(deftest test-get-new-user-id-existence
    "Check that the chainring-service.db-interface/get-new-user-id definition exists."
    (testing "if the chainring-service.db-interface/get-new-user-id definition exists."
        (is (callable? 'chainring-service.db-interface/get-new-user-id))))


(deftest test-get-record-count-existence
    "Check that the chainring-service.db-interface/get-record-count definition exists."
    (testing "if the chainring-service.db-interface/get-record-count definition exists."
        (is (callable? 'chainring-service.db-interface/get-record-count))))


(deftest test-get-db-status-existence
    "Check that the chainring-service.db-interface/get-db-status definition exists."
    (testing "if the chainring-service.db-interface/get-db-status definition exists."
        (is (callable? 'chainring-service.db-interface/get-db-status))))


(deftest test-update-or-insert!-existence
    "Check that the chainring-service.db-interface/update-or-insert! definition exists."
    (testing "if the chainring-service.db-interface/update-or-insert! definition exists."
        (is (callable? 'chainring-service.db-interface/update-or-insert!))))


(deftest test-store-user-settings-existence
    "Check that the chainring-service.db-interface/store-user-settings definition exists."
    (testing "if the chainring-service.db-interface/store-user-settings definition exists."
        (is (callable? 'chainring-service.db-interface/store-user-settings))))

