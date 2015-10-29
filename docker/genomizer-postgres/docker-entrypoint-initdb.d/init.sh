#! /bin/sh

psql -c 'create database genomizerdb;' -U "$POSTGRES_USER"
psql -c 'create user genomizer;' -U "$POSTGRES_USER"
psql -c 'grant all privileges on database genomizerdb to genomizer;' -U "$POSTGRES_USER"

psql -f /sql/genomizer_database_tables.sql -U genomizer -d genomizerdb
psql -f /sql/add_test_tuples.sql -U genomizer -d genomizerdb
# psql -f /sql/release_database_tuples.sql -U genomizer -d genomizerdb
