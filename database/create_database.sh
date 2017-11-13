#!/bin/sh

DATABASE=chainring.db

cat schema.sql | sqlite3 ../${DATABASE}
cat catalog.sql | sqlite3 ../${DATABASE}
cat test_data.sql | sqlite3 ../${DATABASE}

