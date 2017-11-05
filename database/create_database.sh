#!/bin/sh

DATABASE=chainring.db

cat create_database.sql | sqlite3 ../${DATABASE}

