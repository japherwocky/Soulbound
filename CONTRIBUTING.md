# Contributing to Soulbound

Thank you for your interest in contributing to Soulbound! This document provides guidelines and instructions for contributing to this project.

## Development Environment Setup

1. **Prerequisites**
   - Java 21 or higher (required for Paper 1.21.4)
   - Git
   - Gradle 9.0.0 or use the included wrapper

2. **Clone the Repository**
   ```bash
   git clone https://github.com/japherwocky/Soulbound.git
   cd Soulbound
   ```

3. **Build the Project**
   Using the Gradle wrapper:
   ```bash
   ./gradlew build
   ```
   
   Or if you have Gradle 9.0.0 installed:
   ```bash
   gradle build
   ```

4. **Import into your IDE**
   - For IntelliJ IDEA: File > Open > Select the build.gradle file
   - For Eclipse: File > Import > Gradle > Existing Gradle Project

## Making Changes

1. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Your Changes**
   - Follow the existing code style
   - Add appropriate comments
   - Update documentation if necessary

3. **Test Your Changes**
   - Test on a local Paper server
   - Ensure all existing functionality still works
   - Add new tests if applicable

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "Description of your changes"
   ```

5. **Push to GitHub**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request**
   - Go to the repository on GitHub
   - Click "New Pull Request"
   - Select your branch
   - Fill out the PR template

## Code Style Guidelines

- Use 4 spaces for indentation
- Follow Java naming conventions
- Keep methods small and focused
- Add JavaDoc comments to public methods
- Use meaningful variable and method names

## Testing

- Test your changes on Paper 1.21.x
- Verify that the plugin loads correctly
- Test all commands and features
- Check for compatibility with other plugins

## Documentation

- Update README.md if you add new features
- Add JavaDoc comments to new methods
- Update config.yml comments if you change configuration options

## Questions?

If you have any questions or need help, please open an issue on GitHub or contact the maintainers.

Thank you for contributing to Soulbound!
