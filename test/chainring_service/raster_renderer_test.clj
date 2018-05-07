(ns chainring-service.raster-renderer-test
  (:require [clojure.test :refer :all]
            [chainring-service.raster-renderer :refer :all]))

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

(deftest test-proper-scale?-existence
    "Check that the chainring-service.raster-renderer/proper-scale? definition exists."
    (testing "if the chainring-service.raster-renderer/proper-scale? definition exists."
        (is (callable? 'chainring-service.raster-renderer/proper-scale?))))


(deftest test-get-scale-from-scales-existence
    "Check that the chainring-service.raster-renderer/get-scale-from-scales definition exists."
    (testing "if the chainring-service.raster-renderer/get-scale-from-scales definition exists."
        (is (callable? 'chainring-service.raster-renderer/get-scale-from-scales))))


(deftest test-get-scale-existence
    "Check that the chainring-service.raster-renderer/get-scale definition exists."
    (testing "if the chainring-service.raster-renderer/get-scale definition exists."
        (is (callable? 'chainring-service.raster-renderer/get-scale))))


(deftest test-transform-existence
    "Check that the chainring-service.raster-renderer/transform definition exists."
    (testing "if the chainring-service.raster-renderer/transform definition exists."
        (is (callable? 'chainring-service.raster-renderer/transform))))


(deftest test-setup-graphics-context-existence
    "Check that the chainring-service.raster-renderer/setup-graphics-context definition exists."
    (testing "if the chainring-service.raster-renderer/setup-graphics-context definition exists."
        (is (callable? 'chainring-service.raster-renderer/setup-graphics-context))))


(deftest test-draw-line-existence
    "Check that the chainring-service.raster-renderer/draw-line definition exists."
    (testing "if the chainring-service.raster-renderer/draw-line definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-line))))


(deftest test-draw-arc-existence
    "Check that the chainring-service.raster-renderer/draw-arc definition exists."
    (testing "if the chainring-service.raster-renderer/draw-arc definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-arc))))


(deftest test-draw-circle-existence
    "Check that the chainring-service.raster-renderer/draw-circle definition exists."
    (testing "if the chainring-service.raster-renderer/draw-circle definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-circle))))


(deftest test-draw-text-existence
    "Check that the chainring-service.raster-renderer/draw-text definition exists."
    (testing "if the chainring-service.raster-renderer/draw-text definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-text))))


(deftest test-draw-entities-existence
    "Check that the chainring-service.raster-renderer/draw-entities definition exists."
    (testing "if the chainring-service.raster-renderer/draw-entities definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-entities))))


(deftest test-draw-entities-from-binary-file-existence
    "Check that the chainring-service.raster-renderer/draw-entities-from-binary-file definition exists."
    (testing "if the chainring-service.raster-renderer/draw-entities-from-binary-file definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-entities-from-binary-file))))


(deftest test-draw-room-background-existence
    "Check that the chainring-service.raster-renderer/draw-room-background definition exists."
    (testing "if the chainring-service.raster-renderer/draw-room-background definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-room-background))))


(deftest test-draw-room-contour-existence
    "Check that the chainring-service.raster-renderer/draw-room-contour definition exists."
    (testing "if the chainring-service.raster-renderer/draw-room-contour definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-room-contour))))


(deftest test-draw-selected-room-existence
    "Check that the chainring-service.raster-renderer/draw-selected-room definition exists."
    (testing "if the chainring-service.raster-renderer/draw-selected-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-selected-room))))


(deftest test-draw-highlighted-room-existence
    "Check that the chainring-service.raster-renderer/draw-highlighted-room definition exists."
    (testing "if the chainring-service.raster-renderer/draw-highlighted-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-highlighted-room))))


(deftest test-draw-regular-room-existence
    "Check that the chainring-service.raster-renderer/draw-regular-room definition exists."
    (testing "if the chainring-service.raster-renderer/draw-regular-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-regular-room))))


(deftest test-coords-in-polygon-existence
    "Check that the chainring-service.raster-renderer/coords-in-polygon definition exists."
    (testing "if the chainring-service.raster-renderer/coords-in-polygon definition exists."
        (is (callable? 'chainring-service.raster-renderer/coords-in-polygon))))


(deftest test-selected-room?-existence
    "Check that the chainring-service.raster-renderer/selected-room? definition exists."
    (testing "if the chainring-service.raster-renderer/selected-room? definition exists."
        (is (callable? 'chainring-service.raster-renderer/selected-room?))))


(deftest test-highlighted-room?-existence
    "Check that the chainring-service.raster-renderer/highlighted-room? definition exists."
    (testing "if the chainring-service.raster-renderer/highlighted-room? definition exists."
        (is (callable? 'chainring-service.raster-renderer/highlighted-room?))))


(deftest test-draw-room-existence
    "Check that the chainring-service.raster-renderer/draw-room definition exists."
    (testing "if the chainring-service.raster-renderer/draw-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-room))))


(deftest test-draw-rooms-existence
    "Check that the chainring-service.raster-renderer/draw-rooms definition exists."
    (testing "if the chainring-service.raster-renderer/draw-rooms definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-rooms))))


(deftest test-draw-rooms-from-binary-existence
    "Check that the chainring-service.raster-renderer/draw-rooms-from-binary definition exists."
    (testing "if the chainring-service.raster-renderer/draw-rooms-from-binary definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-rooms-from-binary))))


(deftest test-draw-selection-point-existence
    "Check that the chainring-service.raster-renderer/draw-selection-point definition exists."
    (testing "if the chainring-service.raster-renderer/draw-selection-point definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-selection-point))))


(deftest test-drawing-full-name-existence
    "Check that the chainring-service.raster-renderer/drawing-full-name definition exists."
    (testing "if the chainring-service.raster-renderer/drawing-full-name definition exists."
        (is (callable? 'chainring-service.raster-renderer/drawing-full-name))))


(deftest test-drawing-full-name-binary-existence
    "Check that the chainring-service.raster-renderer/drawing-full-name-binary definition exists."
    (testing "if the chainring-service.raster-renderer/drawing-full-name-binary definition exists."
        (is (callable? 'chainring-service.raster-renderer/drawing-full-name-binary))))


(deftest test-read-drawing-from-json-existence
    "Check that the chainring-service.raster-renderer/read-drawing-from-json definition exists."
    (testing "if the chainring-service.raster-renderer/read-drawing-from-json definition exists."
        (is (callable? 'chainring-service.raster-renderer/read-drawing-from-json))))


(deftest test-prepare-data-stream-existence
    "Check that the chainring-service.raster-renderer/prepare-data-stream definition exists."
    (testing "if the chainring-service.raster-renderer/prepare-data-stream definition exists."
        (is (callable? 'chainring-service.raster-renderer/prepare-data-stream))))


(deftest test-draw-into-image-from-binary-data-existence
    "Check that the chainring-service.raster-renderer/draw-into-image-from-binary-data definition exists."
    (testing "if the chainring-service.raster-renderer/draw-into-image-from-binary-data definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-into-image-from-binary-data))))


(deftest test-get-drawing-data-existence
    "Check that the chainring-service.raster-renderer/get-drawing-data definition exists."
    (testing "if the chainring-service.raster-renderer/get-drawing-data definition exists."
        (is (callable? 'chainring-service.raster-renderer/get-drawing-data))))


(deftest test-offset+scale-existence
    "Check that the chainring-service.raster-renderer/offset+scale definition exists."
    (testing "if the chainring-service.raster-renderer/offset+scale definition exists."
        (is (callable? 'chainring-service.raster-renderer/offset+scale))))


(deftest test-draw-into-image-existence
    "Check that the chainring-service.raster-renderer/draw-into-image definition exists."
    (testing "if the chainring-service.raster-renderer/draw-into-image definition exists."
        (is (callable? 'chainring-service.raster-renderer/draw-into-image))))


(deftest test-aoid+selected-existence
    "Check that the chainring-service.raster-renderer/aoid+selected definition exists."
    (testing "if the chainring-service.raster-renderer/aoid+selected definition exists."
        (is (callable? 'chainring-service.raster-renderer/aoid+selected))))


(deftest test-find-room-existence
    "Check that the chainring-service.raster-renderer/find-room definition exists."
    (testing "if the chainring-service.raster-renderer/find-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/find-room))))


(deftest test-color-for-room-capacity-existence
    "Check that the chainring-service.raster-renderer/color-for-room-capacity definition exists."
    (testing "if the chainring-service.raster-renderer/color-for-room-capacity definition exists."
        (is (callable? 'chainring-service.raster-renderer/color-for-room-capacity))))


(deftest test-compute-room-color-existence
    "Check that the chainring-service.raster-renderer/compute-room-color definition exists."
    (testing "if the chainring-service.raster-renderer/compute-room-color definition exists."
        (is (callable? 'chainring-service.raster-renderer/compute-room-color))))


(deftest test-compute-room-colors-existence
    "Check that the chainring-service.raster-renderer/compute-room-colors definition exists."
    (testing "if the chainring-service.raster-renderer/compute-room-colors definition exists."
        (is (callable? 'chainring-service.raster-renderer/compute-room-colors))))


(deftest test-use-binary-rendering?-existence
    "Check that the chainring-service.raster-renderer/use-binary-rendering? definition exists."
    (testing "if the chainring-service.raster-renderer/use-binary-rendering? definition exists."
        (is (callable? 'chainring-service.raster-renderer/use-binary-rendering?))))


(deftest test-perform-raster-drawing-existence
    "Check that the chainring-service.raster-renderer/perform-raster-drawing definition exists."
    (testing "if the chainring-service.raster-renderer/perform-raster-drawing definition exists."
        (is (callable? 'chainring-service.raster-renderer/perform-raster-drawing))))


(deftest test-perform-find-room-existence
    "Check that the chainring-service.raster-renderer/perform-find-room definition exists."
    (testing "if the chainring-service.raster-renderer/perform-find-room definition exists."
        (is (callable? 'chainring-service.raster-renderer/perform-find-room))))


(deftest test-raster-drawing-existence
    "Check that the chainring-service.raster-renderer/raster-drawing definition exists."
    (testing "if the chainring-service.raster-renderer/raster-drawing definition exists."
        (is (callable? 'chainring-service.raster-renderer/raster-drawing))))


(deftest test-find-room-on-drawing-existence
    "Check that the chainring-service.raster-renderer/find-room-on-drawing definition exists."
    (testing "if the chainring-service.raster-renderer/find-room-on-drawing definition exists."
        (is (callable? 'chainring-service.raster-renderer/find-room-on-drawing))))
