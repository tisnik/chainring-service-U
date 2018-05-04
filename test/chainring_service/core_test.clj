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

(ns chainring-service.core-test
  (:require [clojure.test :refer :all]
            [chainring-service.core :refer :all]))

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

(deftest test-start-server-existence
    "Check that the chainring-service.core/start-server definition exists."
    (testing "if the chainring-service.core/start-server definition exists."
        (is (callable? 'chainring-service.core/start-server))))


(deftest test-show-help-existence
    "Check that the chainring-service.core/show-help definition exists."
    (testing "if the chainring-service.core/show-help definition exists."
        (is (callable? 'chainring-service.core/show-help))))


(deftest test-show-configuration-existence
    "Check that the chainring-service.core/show-configuration definition exists."
    (testing "if the chainring-service.core/show-configuration definition exists."
        (is (callable? 'chainring-service.core/show-configuration))))


(deftest test--main-existence
    "Check that the chainring-service.core/-main definition exists."
    (testing "if the chainring-service.core/-main definition exists."
        (is (callable? 'chainring-service.core/-main))))

