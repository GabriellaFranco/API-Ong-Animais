#!/bin/bash
set -e

DB_NAME="ong-pet"

echo "Checking if database '$DB_NAME' exists..."

if psql -U "$POSTGRES_USER" -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'" | grep -q 1; then
  echo "Database '$DB_NAME' already exists. Skipping creation."
else
  echo "Database '$DB_NAME' does not exist. Creating..."
  psql -U "$POSTGRES_USER" -c "CREATE DATABASE \"$DB_NAME\";"
  echo "Database '$DB_NAME' created successfully."
fi