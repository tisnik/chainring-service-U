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

(deftest test-get-user-id-existence
    "Check that the chainring-service.server/get-user-id definition exists."
    (testing "if the chainring-service.server/get-user-id definition exists."
        (is (callable? 'chainring-service.server/get-user-id))))


(deftest test-finish-processing-existence
    "Check that the chainring-service.server/finish-processing definition exists."
    (testing "if the chainring-service.server/finish-processing definition exists."
        (is (callable? 'chainring-service.server/finish-processing))))


(deftest test-process-front-page-existence
    "Check that the chainring-service.server/process-front-page definition exists."
    (testing "if the chainring-service.server/process-front-page definition exists."
        (is (callable? 'chainring-service.server/process-front-page))))


(deftest test-process-settings-page-existence
    "Check that the chainring-service.server/process-settings-page definition exists."
    (testing "if the chainring-service.server/process-settings-page definition exists."
        (is (callable? 'chainring-service.server/process-settings-page))))


(deftest test-process-store-settings-page-existence
    "Check that the chainring-service.server/process-store-settings-page definition exists."
    (testing "if the chainring-service.server/process-store-settings-page definition exists."
        (is (callable? 'chainring-service.server/process-store-settings-page))))


(deftest test-process-db-statistic-page-existence
    "Check that the chainring-service.server/process-db-statistic-page definition exists."
    (testing "if the chainring-service.server/process-db-statistic-page definition exists."
        (is (callable? 'chainring-service.server/process-db-statistic-page))))


(deftest test-process-drawings-statistic-page-existence
    "Check that the chainring-service.server/process-drawings-statistic-page definition exists."
    (testing "if the chainring-service.server/process-drawings-statistic-page definition exists."
        (is (callable? 'chainring-service.server/process-drawings-statistic-page))))


(deftest test-process-drawings-list-existence
    "Check that the chainring-service.server/process-drawings-list definition exists."
    (testing "if the chainring-service.server/process-drawings-list definition exists."
        (is (callable? 'chainring-service.server/process-drawings-list))))


(deftest test-process-json-list-existence
    "Check that the chainring-service.server/process-json-list definition exists."
    (testing "if the chainring-service.server/process-json-list definition exists."
        (is (callable? 'chainring-service.server/process-json-list))))


(deftest test-process-binary-list-existence
    "Check that the chainring-service.server/process-binary-list definition exists."
    (testing "if the chainring-service.server/process-binary-list definition exists."
        (is (callable? 'chainring-service.server/process-binary-list))))


(deftest test-process-project-list-page-existence
    "Check that the chainring-service.server/process-project-list-page definition exists."
    (testing "if the chainring-service.server/process-project-list-page definition exists."
        (is (callable? 'chainring-service.server/process-project-list-page))))


(deftest test-process-project-info-page-existence
    "Check that the chainring-service.server/process-project-info-page definition exists."
    (testing "if the chainring-service.server/process-project-info-page definition exists."
        (is (callable? 'chainring-service.server/process-project-info-page))))


(deftest test-process-building-info-page-existence
    "Check that the chainring-service.server/process-building-info-page definition exists."
    (testing "if the chainring-service.server/process-building-info-page definition exists."
        (is (callable? 'chainring-service.server/process-building-info-page))))


(deftest test-process-floor-info-page-existence
    "Check that the chainring-service.server/process-floor-info-page definition exists."
    (testing "if the chainring-service.server/process-floor-info-page definition exists."
        (is (callable? 'chainring-service.server/process-floor-info-page))))


(deftest test-process-room-list-existence
    "Check that the chainring-service.server/process-room-list definition exists."
    (testing "if the chainring-service.server/process-room-list definition exists."
        (is (callable? 'chainring-service.server/process-room-list))))


(deftest test-process-project-page-existence
    "Check that the chainring-service.server/process-project-page definition exists."
    (testing "if the chainring-service.server/process-project-page definition exists."
        (is (callable? 'chainring-service.server/process-project-page))))


(deftest test-process-building-page-existence
    "Check that the chainring-service.server/process-building-page definition exists."
    (testing "if the chainring-service.server/process-building-page definition exists."
        (is (callable? 'chainring-service.server/process-building-page))))


(deftest test-process-floor-page-existence
    "Check that the chainring-service.server/process-floor-page definition exists."
    (testing "if the chainring-service.server/process-floor-page definition exists."
        (is (callable? 'chainring-service.server/process-floor-page))))


(deftest test-process-drawing-preview-page-existence
    "Check that the chainring-service.server/process-drawing-preview-page definition exists."
    (testing "if the chainring-service.server/process-drawing-preview-page definition exists."
        (is (callable? 'chainring-service.server/process-drawing-preview-page))))


(deftest test-process-raster-preview-page-existence
    "Check that the chainring-service.server/process-raster-preview-page definition exists."
    (testing "if the chainring-service.server/process-raster-preview-page definition exists."
        (is (callable? 'chainring-service.server/process-raster-preview-page))))


(deftest test-process-drawing-page-existence
    "Check that the chainring-service.server/process-drawing-page definition exists."
    (testing "if the chainring-service.server/process-drawing-page definition exists."
        (is (callable? 'chainring-service.server/process-drawing-page))))


(deftest test-get-api-part-from-uri-existence
    "Check that the chainring-service.server/get-api-part-from-uri definition exists."
    (testing "if the chainring-service.server/get-api-part-from-uri definition exists."
        (is (callable? 'chainring-service.server/get-api-part-from-uri))))


(deftest test-get-api-command-existence
    "Check that the chainring-service.server/get-api-command definition exists."
    (testing "if the chainring-service.server/get-api-command definition exists."
        (is (callable? 'chainring-service.server/get-api-command))))


(deftest test-api-call-handler-existence
    "Check that the chainring-service.server/api-call-handler definition exists."
    (testing "if the chainring-service.server/api-call-handler definition exists."
        (is (callable? 'chainring-service.server/api-call-handler))))


(deftest test-uri->file-name-existence
    "Check that the chainring-service.server/uri->file-name definition exists."
    (testing "if the chainring-service.server/uri->file-name definition exists."
        (is (callable? 'chainring-service.server/uri->file-name))))


(deftest test-gui-call-handler-existence
    "Check that the chainring-service.server/gui-call-handler definition exists."
    (testing "if the chainring-service.server/gui-call-handler definition exists."
        (is (callable? 'chainring-service.server/gui-call-handler))))


(deftest test-handler-existence
    "Check that the chainring-service.server/handler definition exists."
    (testing "if the chainring-service.server/handler definition exists."
        (is (callable? 'chainring-service.server/handler))))

