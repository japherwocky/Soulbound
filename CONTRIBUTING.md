# Contributing to Soulbound

Thank you for your interest in contributing to Soulbound! This document provides guidelines and instructions for contributing to this multi-platform Minecraft mod/plugin.

## Repository Structure

This repository is organized using branches to support multiple Minecraft versions and platforms:

```
master (documentation, shared resources)
├── mc-1.17/fabric-0.11.x
├── mc-1.17/spigot-1.17.x
└── ...
```

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/Soulbound.git`
3. Checkout the branch for the platform and Minecraft version you want to work on:
   ```bash
   git checkout mc-1.17/fabric-0.11.x  # or another branch
   ```
4. Create a new branch for your feature or bugfix:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Making Changes

### For All Platforms

- Follow the coding style of the platform you're working on
- Write clear commit messages
- Add tests for your changes when possible
- Update documentation as needed

### For Fabric

- Follow the Fabric mod development guidelines
- Test your changes with the Fabric loader

### For Spigot/Paper

- Follow the Bukkit/Spigot plugin development guidelines
- Test your changes on a Spigot or Paper server

## Shared Resources

Common resources like language files and assets are stored in the `common` directory on the master branch. If you need to update these:

1. Checkout the master branch
2. Make your changes to the files in the `common` directory
3. Commit and push your changes
4. Create a pull request for the master branch

## Submitting Changes

1. Push your changes to your fork
2. Create a pull request against the appropriate branch (not master)
3. Describe your changes in detail
4. Reference any related issues

## Code Review

All submissions require review. We use GitHub pull requests for this purpose.

## Additional Resources

- [Fabric Documentation](https://fabricmc.net/wiki/start)
- [Spigot Plugin Development](https://www.spigotmc.org/wiki/spigot-plugin-development/)
- [Bukkit JavaDocs](https://hub.spigotmc.org/javadocs/bukkit/)

Thank you for contributing to Soulbound!

