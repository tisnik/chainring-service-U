(ns chainring-service.drawings-storage-test
  (:require [clojure.test :refer :all]
            [chainring-service.drawings-storage :refer :all]))

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

(deftest test-store-drawing-as-json-existence
    "Check that the chainring-service.drawings-storage/store-drawing-as-json definition exists."
    (testing "if the chainring-service.drawings-storage/store-drawing-as-json definition exists."
        (is (callable? 'chainring-service.drawings-storage/store-drawing-as-json))))


(deftest test-store-drawing-as-edn-existence
    "Check that the chainring-service.drawings-storage/store-drawing-as-edn definition exists."
    (testing "if the chainring-service.drawings-storage/store-drawing-as-edn definition exists."
        (is (callable? 'chainring-service.drawings-storage/store-drawing-as-edn))))


(deftest test-string->date-existence
    "Check that the chainring-service.drawings-storage/string->date definition exists."
    (testing "if the chainring-service.drawings-storage/string->date definition exists."
        (is (callable? 'chainring-service.drawings-storage/string->date))))


(deftest test-write-binary-header-existence
    "Check that the chainring-service.drawings-storage/write-binary-header definition exists."
    (testing "if the chainring-service.drawings-storage/write-binary-header definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-binary-header))))


(deftest test-write-created-existence
    "Check that the chainring-service.drawings-storage/write-created definition exists."
    (testing "if the chainring-service.drawings-storage/write-created definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-created))))


(deftest test-write-counters-existence
    "Check that the chainring-service.drawings-storage/write-counters definition exists."
    (testing "if the chainring-service.drawings-storage/write-counters definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-counters))))


(deftest test-write-bounds-existence
    "Check that the chainring-service.drawings-storage/write-bounds definition exists."
    (testing "if the chainring-service.drawings-storage/write-bounds definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-bounds))))


(deftest test-write-scales-existence
    "Check that the chainring-service.drawings-storage/write-scales definition exists."
    (testing "if the chainring-service.drawings-storage/write-scales definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-scales))))


(deftest test-write-line-existence
    "Check that the chainring-service.drawings-storage/write-line definition exists."
    (testing "if the chainring-service.drawings-storage/write-line definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-line))))


(deftest test-write-circle-existence
    "Check that the chainring-service.drawings-storage/write-circle definition exists."
    (testing "if the chainring-service.drawings-storage/write-circle definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-circle))))


(deftest test-write-arc-existence
    "Check that the chainring-service.drawings-storage/write-arc definition exists."
    (testing "if the chainring-service.drawings-storage/write-arc definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-arc))))


(deftest test-write-text-existence
    "Check that the chainring-service.drawings-storage/write-text definition exists."
    (testing "if the chainring-service.drawings-storage/write-text definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-text))))


(deftest test-write-entity-existence
    "Check that the chainring-service.drawings-storage/write-entity definition exists."
    (testing "if the chainring-service.drawings-storage/write-entity definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-entity))))


(deftest test-write-entities-existence
    "Check that the chainring-service.drawings-storage/write-entities definition exists."
    (testing "if the chainring-service.drawings-storage/write-entities definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-entities))))


(deftest test-write-room-existence
    "Check that the chainring-service.drawings-storage/write-room definition exists."
    (testing "if the chainring-service.drawings-storage/write-room definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-room))))


(deftest test-write-rooms-existence
    "Check that the chainring-service.drawings-storage/write-rooms definition exists."
    (testing "if the chainring-service.drawings-storage/write-rooms definition exists."
        (is (callable? 'chainring-service.drawings-storage/write-rooms))))


(deftest test-store-drawing-as-binary-existence
    "Check that the chainring-service.drawings-storage/store-drawing-as-binary definition exists."
    (testing "if the chainring-service.drawings-storage/store-drawing-as-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/store-drawing-as-binary))))


(deftest test-store-drawing-as-existence
    "Check that the chainring-service.drawings-storage/store-drawing-as definition exists."
    (testing "if the chainring-service.drawings-storage/store-drawing-as definition exists."
        (is (callable? 'chainring-service.drawings-storage/store-drawing-as))))


(deftest test-read-binary-header-existence
    "Check that the chainring-service.drawings-storage/read-binary-header definition exists."
    (testing "if the chainring-service.drawings-storage/read-binary-header definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-binary-header))))


(deftest test-read-created-time-existence
    "Check that the chainring-service.drawings-storage/read-created-time definition exists."
    (testing "if the chainring-service.drawings-storage/read-created-time definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-created-time))))


(deftest test-read-counters-existence
    "Check that the chainring-service.drawings-storage/read-counters definition exists."
    (testing "if the chainring-service.drawings-storage/read-counters definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-counters))))


(deftest test-read-bounds-existence
    "Check that the chainring-service.drawings-storage/read-bounds definition exists."
    (testing "if the chainring-service.drawings-storage/read-bounds definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-bounds))))


(deftest test-read-scales-existence
    "Check that the chainring-service.drawings-storage/read-scales definition exists."
    (testing "if the chainring-service.drawings-storage/read-scales definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-scales))))


(deftest test-read-line-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-line-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-line-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-line-from-binary))))


(deftest test-read-circle-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-circle-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-circle-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-circle-from-binary))))


(deftest test-read-arc-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-arc-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-arc-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-arc-from-binary))))


(deftest test-read-text-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-text-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-text-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-text-from-binary))))


(deftest test-read-entity-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-entity-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-entity-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-entity-from-binary))))


(deftest test-read-room-polygon-existence
    "Check that the chainring-service.drawings-storage/read-room-polygon definition exists."
    (testing "if the chainring-service.drawings-storage/read-room-polygon definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-room-polygon))))


(deftest test-read-room-from-binary-existence
    "Check that the chainring-service.drawings-storage/read-room-from-binary definition exists."
    (testing "if the chainring-service.drawings-storage/read-room-from-binary definition exists."
        (is (callable? 'chainring-service.drawings-storage/read-room-from-binary))))

