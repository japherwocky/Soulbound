@echo off
REM Build script for Soulbound plugin

REM Check if JAVA_HOME is already set
if defined JAVA_HOME (
    echo Using JAVA_HOME from environment: %JAVA_HOME%
) else (
    REM Try to detect Java location if JAVA_HOME is not set
    where java >nul 2>nul
    if %ERRORLEVEL% == 0 (
        for /f "tokens=*" %%i in ('where java') do set JAVA_PATH=%%i
        for %%i in ("%JAVA_PATH%") do set JAVA_BIN_DIR=%%~dpi
        for %%i in ("%JAVA_BIN_DIR%..") do set JAVA_HOME=%%~fi
        echo Auto-detected Java at: %JAVA_HOME%
    ) else (
        echo Warning: Java not found in PATH and JAVA_HOME is not set.
        echo Please set JAVA_HOME environment variable to your Java installation directory.
    )
)

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
%GRADLE_CMD% clean build --info

REM Check if build was successful
if %ERRORLEVEL% == 0 (
    echo Build successful!
    echo JAR file created in build\libs directory
) else (
    echo Build failed. Please check the error messages above.
    exit /b 1
)
