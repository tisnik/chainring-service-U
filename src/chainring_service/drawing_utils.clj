;
;  (C) Copyright 2017, 2018  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns chainring-service.drawing-utils)

(defn filename->drawing-version
    [filename floor-id]
    (let [wo-prefix (subs filename (inc (count floor-id)))
          wo-ext    (subs wo-prefix 0 (- (count wo-prefix) (count ".json")))]
          wo-ext))


(defn filename->valid-from
    [filename floor-id]
    (let [wo-prefix (subs filename (inc (count floor-id)))
          wo-ext    (subs wo-prefix 0 (- (count wo-prefix) (count ".json")))]
          (str (subs wo-ext 0 4) "-" (subs wo-ext 4 6) "-" (subs wo-ext 6 8))))


(defn filename->drawing-id
    [filename]
    (subs filename 0 (- (count filename) (count ".json"))))
