#!/bin/bash

echo "=== Building ACME Medical Application ==="

# Build the application
echo "Building application with Maven..."
mvn clean package -DskipTests

echo "=== Build completed ==="