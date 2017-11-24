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

(ns chainring-service.utils-test
  (:require [clojure.test :refer :all]
            [chainring-service.utils :refer :all]))

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

(deftest test-third-existence
    "Check that the chainring-service.utils/third definition exists."
    (testing "if the chainring-service.utils/third definition exists."
        (is (callable? 'chainring-service.utils/third))))


(deftest test-fourth-existence
    "Check that the chainring-service.utils/fourth definition exists."
    (testing "if the chainring-service.utils/fourth definition exists."
        (is (callable? 'chainring-service.utils/fourth))))


(deftest test-substring-existence
    "Check that the chainring-service.utils/substring definition exists."
    (testing "if the chainring-service.utils/substring definition exists."
        (is (callable? 'chainring-service.utils/substring))))


(deftest test-startsWith-existence
    "Check that the chainring-service.utils/startsWith definition exists."
    (testing "if the chainring-service.utils/startsWith definition exists."
        (is (callable? 'chainring-service.utils/startsWith))))


(deftest test-endsWith-existence
    "Check that the chainring-service.utils/endsWith definition exists."
    (testing "if the chainring-service.utils/endsWith definition exists."
        (is (callable? 'chainring-service.utils/endsWith))))


(deftest test-contains-existence
    "Check that the chainring-service.utils/contains definition exists."
    (testing "if the chainring-service.utils/contains definition exists."
        (is (callable? 'chainring-service.utils/contains))))


(deftest test-replaceAll-existence
    "Check that the chainring-service.utils/replaceAll definition exists."
    (testing "if the chainring-service.utils/replaceAll definition exists."
        (is (callable? 'chainring-service.utils/replaceAll))))


(deftest test-get-exception-message-existence
    "Check that the chainring-service.utils/get-exception-message definition exists."
    (testing "if the chainring-service.utils/get-exception-message definition exists."
        (is (callable? 'chainring-service.utils/get-exception-message))))


(deftest test-throw-exception-existence
    "Check that the chainring-service.utils/throw-exception definition exists."
    (testing "if the chainring-service.utils/throw-exception definition exists."
        (is (callable? 'chainring-service.utils/throw-exception))))


(deftest test-parse-int-existence
    "Check that the chainring-service.utils/parse-int definition exists."
    (testing "if the chainring-service.utils/parse-int definition exists."
        (is (callable? 'chainring-service.utils/parse-int))))


(deftest test-parse-float-existence
    "Check that the chainring-service.utils/parse-float definition exists."
    (testing "if the chainring-service.utils/parse-float definition exists."
        (is (callable? 'chainring-service.utils/parse-float))))


(deftest test-parse-boolean-existence
    "Check that the chainring-service.utils/parse-boolean definition exists."
    (testing "if the chainring-service.utils/parse-boolean definition exists."
        (is (callable? 'chainring-service.utils/parse-boolean))))



