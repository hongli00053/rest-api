#!/bin/bash

echo "=== Building ACME Medical Application ==="

# Download Payara Micro if not exists
if [ ! -f "payara-micro.jar" ]; then
    echo "Downloading Payara Micro..."
    curl -L -o payara-micro.jar https://repo1.maven.org/maven2/fish/payara/extras/payara-micro/6.2023.12/payara-micro-6.2023.12.jar
    echo "Payara Micro downloaded successfully"
fi

# Build the application
echo "Building application with Maven..."
mvn clean package -DskipTests

echo "=== Build completed ==="

