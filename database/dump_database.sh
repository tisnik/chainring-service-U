#!/bin/sh

DATABASE=chainring.db

cat dump_database.sql | sqlite3 ../${DATABASE}

