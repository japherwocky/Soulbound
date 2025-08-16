@echo off
REM Build script for Soulbound plugin

REM Check if Gradle is available in .\lib\gradle-9.0.0\bin
if exist ".\lib\gradle-9.0.0\bin\gradle.bat" (
    echo Using Gradle 9.0.0 from .\lib directory
    set GRADLE_CMD=.\lib\gradle-9.0.0\bin\gradle.bat
REM Check if Gradle wrapper is available
) else if exist ".\gradlew.bat" (
    echo Using Gradle wrapper
    set GRADLE_CMD=.\gradlew.bat
REM Check if Gradle is available on PATH
) else (
    where gradle >nul 2>nul
    if %ERRORLEVEL% == 0 (
        echo Using Gradle from PATH
        set GRADLE_CMD=gradle
    ) else (
        echo Error: Gradle not found. Please install Gradle or use the provided wrapper.
        exit /b 1
    )
)

REM Clean and build the project
echo Building Soulbound plugin...
%GRADLE_CMD% clean build

REM Check if build was successful
if %ERRORLEVEL% == 0 (
    echo Build successful!
    echo JAR file created in build\libs directory
) else (
    echo Build failed. Please check the error messages above.
    exit /b 1
)

