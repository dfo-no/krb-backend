#!/bin/bash

# This script deploys any changes in the database that have not been deployed yet.
# The liquibase update command compares id, author, filename and unique checksum
# Prerequisite: https://www.liquibase.com/download

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

liquibase \
--driver=org.postgresql.Driver \
--changelogFile=src/main/resources/db/changeLog.sql \
--url="$DB_JDBC_URL/$DB_DATABASE" \
--username="$DB_USER" \
--password="$DB_PASSWORD" \
update