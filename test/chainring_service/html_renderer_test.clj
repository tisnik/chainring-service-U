(ns chainring-service.html-renderer-test
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


(deftest test-render-html-header-existence
    "Check that the chainring-service.html-renderer/render-html-header definition exists."
    (testing "if the chainring-service.html-renderer/render-html-header definition exists."
        (is (callable? 'chainring-service.html-renderer/render-html-header))))


(deftest test-render-html-footer-existence
    "Check that the chainring-service.html-renderer/render-html-footer definition exists."
    (testing "if the chainring-service.html-renderer/render-html-footer definition exists."
        (is (callable? 'chainring-service.html-renderer/render-html-footer))))


(deftest test-render-navigation-bar-section-existence
    "Check that the chainring-service.html-renderer/render-navigation-bar-section definition exists."
    (testing "if the chainring-service.html-renderer/render-navigation-bar-section definition exists."
        (is (callable? 'chainring-service.html-renderer/render-navigation-bar-section))))


(deftest test-render-front-page-existence
    "Check that the chainring-service.html-renderer/render-front-page definition exists."
    (testing "if the chainring-service.html-renderer/render-front-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-front-page))))


(deftest test-passive-color-box-existence
    "Check that the chainring-service.html-renderer/passive-color-box definition exists."
    (testing "if the chainring-service.html-renderer/passive-color-box definition exists."
        (is (callable? 'chainring-service.html-renderer/passive-color-box))))


(deftest test-color-box-existence
    "Check that the chainring-service.html-renderer/color-box definition exists."
    (testing "if the chainring-service.html-renderer/color-box definition exists."
        (is (callable? 'chainring-service.html-renderer/color-box))))


(deftest test-render-settings-page-existence
    "Check that the chainring-service.html-renderer/render-settings-page definition exists."
    (testing "if the chainring-service.html-renderer/render-settings-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-settings-page))))


(deftest test-render-db-statistic-page-existence
    "Check that the chainring-service.html-renderer/render-db-statistic-page definition exists."
    (testing "if the chainring-service.html-renderer/render-db-statistic-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-db-statistic-page))))


(deftest test-render-drawings-statistic-page-existence
    "Check that the chainring-service.html-renderer/render-drawings-statistic-page definition exists."
    (testing "if the chainring-service.html-renderer/render-drawings-statistic-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-drawings-statistic-page))))


(deftest test-render-drawings-list-existence
    "Check that the chainring-service.html-renderer/render-drawings-list definition exists."
    (testing "if the chainring-service.html-renderer/render-drawings-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-drawings-list))))


(deftest test-render-json-list-existence
    "Check that the chainring-service.html-renderer/render-json-list definition exists."
    (testing "if the chainring-service.html-renderer/render-json-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-json-list))))


(deftest test-render-binary-list-existence
    "Check that the chainring-service.html-renderer/render-binary-list definition exists."
    (testing "if the chainring-service.html-renderer/render-binary-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-binary-list))))


(deftest test-render-project-list-existence
    "Check that the chainring-service.html-renderer/render-project-list definition exists."
    (testing "if the chainring-service.html-renderer/render-project-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-project-list))))


(deftest test-render-project-info-existence
    "Check that the chainring-service.html-renderer/render-project-info definition exists."
    (testing "if the chainring-service.html-renderer/render-project-info definition exists."
        (is (callable? 'chainring-service.html-renderer/render-project-info))))


(deftest test-render-building-info-existence
    "Check that the chainring-service.html-renderer/render-building-info definition exists."
    (testing "if the chainring-service.html-renderer/render-building-info definition exists."
        (is (callable? 'chainring-service.html-renderer/render-building-info))))


(deftest test-render-floor-info-existence
    "Check that the chainring-service.html-renderer/render-floor-info definition exists."
    (testing "if the chainring-service.html-renderer/render-floor-info definition exists."
        (is (callable? 'chainring-service.html-renderer/render-floor-info))))


(deftest test-render-building-list-existence
    "Check that the chainring-service.html-renderer/render-building-list definition exists."
    (testing "if the chainring-service.html-renderer/render-building-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-building-list))))


(deftest test-render-floor-list-existence
    "Check that the chainring-service.html-renderer/render-floor-list definition exists."
    (testing "if the chainring-service.html-renderer/render-floor-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-floor-list))))


(deftest test-sap-href-existence
    "Check that the chainring-service.html-renderer/sap-href definition exists."
    (testing "if the chainring-service.html-renderer/sap-href definition exists."
        (is (callable? 'chainring-service.html-renderer/sap-href))))


(deftest test-render-room-list-existence
    "Check that the chainring-service.html-renderer/render-room-list definition exists."
    (testing "if the chainring-service.html-renderer/render-room-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-room-list))))


(deftest test-render-drawing-list-existence
    "Check that the chainring-service.html-renderer/render-drawing-list definition exists."
    (testing "if the chainring-service.html-renderer/render-drawing-list definition exists."
        (is (callable? 'chainring-service.html-renderer/render-drawing-list))))


(deftest test-render-drawing-preview-existence
    "Check that the chainring-service.html-renderer/render-drawing-preview definition exists."
    (testing "if the chainring-service.html-renderer/render-drawing-preview definition exists."
        (is (callable? 'chainring-service.html-renderer/render-drawing-preview))))


(deftest test-render-drawing-existence
    "Check that the chainring-service.html-renderer/render-drawing definition exists."
    (testing "if the chainring-service.html-renderer/render-drawing definition exists."
        (is (callable? 'chainring-service.html-renderer/render-drawing))))


(deftest test-render-raster-preview-existence
    "Check that the chainring-service.html-renderer/render-raster-preview definition exists."
    (testing "if the chainring-service.html-renderer/render-raster-preview definition exists."
        (is (callable? 'chainring-service.html-renderer/render-raster-preview))))


(deftest test-render-error-page-existence
    "Check that the chainring-service.html-renderer/render-error-page definition exists."
    (testing "if the chainring-service.html-renderer/render-error-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-error-page))))


(deftest test-render-store-settings-page-existence
    "Check that the chainring-service.html-renderer/render-store-settings-page definition exists."
    (testing "if the chainring-service.html-renderer/render-store-settings-page definition exists."
        (is (callable? 'chainring-service.html-renderer/render-store-settings-page))))

