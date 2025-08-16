# Building Soulbound

This document provides detailed instructions for building the Soulbound plugin from source.

## Prerequisites

- Java 21 or higher (required for Paper 1.21.4)
- Gradle 9.0.0 (recommended) or use the included wrapper

## Build Options

### Option 1: Using the Provided Build Scripts

#### For Windows:
```
.\build.bat
```

#### For Linux/macOS:
```
./build.sh
```

These scripts will automatically detect and use the appropriate Gradle installation:
1. First, they check for Gradle 9.0.0 in the `./lib/gradle-9.0.0/bin` directory
2. If not found, they try to use the Gradle wrapper (`gradlew` or `gradlew.bat`)
3. If neither is available, they attempt to use Gradle from your PATH

### Option 2: Using Gradle Directly

#### If you have Gradle 9.0.0 in the lib directory:
```
.\lib\gradle-9.0.0\bin\gradle clean build
```

#### Using the Gradle wrapper:
```
./gradlew clean build
```
or on Windows:
```
.\gradlew.bat clean build
```

#### Using Gradle from PATH:
```
gradle clean build
```

## Build Output

After a successful build, you'll find the plugin JAR file in the `build/libs` directory. The file will be named `Soulbound-<version>.jar`.

## Troubleshooting

### Java Version Issues
Make sure you're using Java 21 or higher. You can check your Java version with:
```
java -version
```

### Gradle Wrapper Issues
If the Gradle wrapper fails, you can regenerate it with:
```
gradle wrapper --gradle-version 9.0.0
```
(This requires having some version of Gradle installed)

### Using a Local Gradle Installation
If you want to use Gradle 9.0.0 from the lib directory:

1. Download Gradle 9.0.0 from https://gradle.org/releases/
2. Extract it to the `lib` directory so that you have `lib/gradle-9.0.0/`
3. Make sure the bin directory contains executable files:
   ```
   chmod +x lib/gradle-9.0.0/bin/gradle
   ```
   (Not needed on Windows)

## Testing the Built Plugin

To test the plugin:

1. Copy the JAR file from `build/libs` to the `plugins` folder of your Paper server
2. Start the server
3. Check the server logs to ensure the plugin loads correctly

## Continuous Integration

For automated builds, we recommend using GitHub Actions. A sample workflow is included in the `.github/workflows` directory.
