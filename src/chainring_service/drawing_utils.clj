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

(ns chainring-service.drawing-utils
    "Various utility functions for handling drawings.

    Author: Pavel Tisnovsky")


(defn filename->drawing-version
    "Get drawing version from a full filename for given floor ID."
    [filename floor-id]
    (let [wo-prefix (subs filename (inc (count floor-id)))
          wo-ext    (subs wo-prefix 0 (- (count wo-prefix) (count ".json")))]
          wo-ext))


(defn filename->valid-from
    "Get drawing valid-from date from a full filename for a given floor ID."
    [filename floor-id]
    (let [wo-prefix (subs filename (inc (count floor-id)))
          wo-ext    (subs wo-prefix 0 (- (count wo-prefix) (count ".json")))]
          (str (subs wo-ext 0 4) "-" (subs wo-ext 4 6) "-" (subs wo-ext 6 8))))


(defn filename->drawing-id
    "Get drawing ID from a full filename."
    [filename]
    (subs filename 0 (- (count filename) (count ".json"))))
