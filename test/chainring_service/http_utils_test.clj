(ns chainring-service.http-utils-test
  (:require [clojure.test :refer :all]
            [chainring-service.http-utils :refer :all]))

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

(deftest test-return-file-existence
    "Check that the chainring-service.http-utils/return-file definition exists."
    (testing "if the chainring-service.http-utils/return-file definition exists."
        (is (callable? 'chainring-service.http-utils/return-file))))

