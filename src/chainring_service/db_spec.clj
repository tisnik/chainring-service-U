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

(ns chainring-service.db-spec
    "Namespace that contains configuration of all JDBC sources.

    Author: Pavel Tisnovsky")


(def chainring-db
    "Specification for SQLite database used for storing info about buldings, floors, and drawings.
     This database can be used to store drawings as well (but the system must be configured to do so)."
    {:classname   "org.sqlite.JDBC"
     :subprotocol "sqlite"
     :subname     "chainring.db"
    })

