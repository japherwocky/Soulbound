#!/bin/bash

# Build script for Soulbound plugin

# Check if Gradle is available in ./lib/gradle-9.0.0/bin
if [ -f "./lib/gradle-9.0.0/bin/gradle" ]; then
    echo "Using Gradle 9.0.0 from ./lib directory"
    GRADLE_CMD="./lib/gradle-9.0.0/bin/gradle"
# Check if Gradle wrapper is available
elif [ -f "./gradlew" ]; then
    echo "Using Gradle wrapper"
    GRADLE_CMD="./gradlew"
# Check if Gradle is available on PATH
elif command -v gradle &> /dev/null; then
    echo "Using Gradle from PATH"
    GRADLE_CMD="gradle"
else
    echo "Error: Gradle not found. Please install Gradle or use the provided wrapper."
    exit 1
fi

# Clean and build the project
echo "Building Soulbound plugin..."
$GRADLE_CMD clean build

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "JAR file created at: build/libs/Soulbound-$(grep 'version =' build.gradle | cut -d "'" -f 2).jar"
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi

