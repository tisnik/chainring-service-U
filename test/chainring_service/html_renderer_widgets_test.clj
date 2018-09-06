(ns chainring-service.html-renderer-widgets-test
  (:require [clojure.test :refer :all]
            [chainring-service.html-renderer-widgets :refer :all]))

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


(deftest test-header-existence
    "Check that the chainring-service.html-renderer-widgets/header definition exists."
    (testing "if the chainring-service.html-renderer-widgets/header definition exists."
        (is (callable? 'chainring-service.html-renderer-widgets/header))))


(deftest test-footer-existence
    "Check that the chainring-service.html-renderer-widgets/footer definition exists."
    (testing "if the chainring-service.html-renderer-widgets/footer definition exists."
        (is (callable? 'chainring-service.html-renderer-widgets/footer))))


(deftest test-navigation-bar-existence
    "Check that the chainring-service.html-renderer-widgets/navigation-bar definition exists."
    (testing "if the chainring-service.html-renderer-widgets/navigation-bar definition exists."
        (is (callable? 'chainring-service.html-renderer-widgets/navigation-bar))))

