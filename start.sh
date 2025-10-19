#!/bin/bash

echo "=== Starting ACME Medical Application ==="

# Set database connection properties from environment variables
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_NAME=${DB_NAME:-acmemedical}
DB_USER=${DB_USER:-cst8277}
DB_PASSWORD=${DB_PASSWORD:-8277}
PORT=${PORT:-8080}

echo "Database Configuration:"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo "  Database: $DB_NAME"
echo "  User: $DB_USER"
echo "Application will start on port: $PORT"

# Start Payara Micro with the WAR file
java -jar payara-micro.jar \
  --deploy target/rest-acmemedical-0.0.1-SNAPSHOT.war \
  --port $PORT \
  --contextroot / \
  --noCluster \
  --systemproperties db.host=$DB_HOST,db.port=$DB_PORT,db.name=$DB_NAME,db.user=$DB_USER,db.password=$DB_PASSWORD

