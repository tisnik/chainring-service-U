;
;  (C) Copyright 2017  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns chainring-service.config-loader-test
  (:require [clojure.test :refer :all]
            [chainring-service.config-loader :refer :all]))

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
(deftest test-properties->map-existence
    "Check that the chainring-service.config-loader/properties->map definition exists."
    (testing "if the chainring-service.config-loader/properties->map definition exists."
        (is (callable? 'chainring-service.config-loader/properties->map))))


(deftest test-load-property-file-existence
    "Check that the chainring-service.config-loader/load-property-file definition exists."
    (testing "if the chainring-service.config-loader/load-property-file definition exists."
        (is (callable? 'chainring-service.config-loader/load-property-file))))


(deftest test-load-configuration-file-existence
    "Check that the chainring-service.config-loader/load-configuration-file definition exists."
    (testing "if the chainring-service.config-loader/load-configuration-file definition exists."
        (is (callable? 'chainring-service.config-loader/load-configuration-file))))

;
; Tests for functions behaviour
;


