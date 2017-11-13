#!/bin/sh

DATABASE=chainring.db

sqlite3 ../${DATABASE} "vacuum"

