# Soulbound

A Minecraft enchantment for keeping items when you die.

## Description

Soulbound is a Paper plugin that adds a new enchantment to Minecraft. When an item has the Soulbound enchantment, it will be retained in the player's inventory upon death instead of dropping on the ground.

## Features

- Adds a new "Soulbound" enchantment
- Items with this enchantment are kept in inventory on death
- Configurable chance for the enchantment to be removed on death
- Option to allow the enchantment on all items or only enchantable items
- API for other plugins to interact with Soulbound items

## Installation

1. Download the latest release from the [Releases](https://github.com/japherwocky/Soulbound/releases) page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/Soulbound/config.yml` (optional)

## Configuration

```yaml
# The chance (0.0 - 1.0) that the Soulbound enchantment will be removed from an item upon death
soulbound-removal-chance: 0.0

# Whether to allow the Soulbound enchantment on all items
allow-on-all-items: true

# Enable debug logging
debug: false
```

## Commands

- `/soulbound` - Shows plugin information
- `/soulbound reload` - Reloads the configuration (requires `soulbound.command` permission)
- `/soulbound info` - Shows current configuration settings (requires `soulbound.command` permission)

## Permissions

- `soulbound.command` - Allows access to Soulbound commands (default: op)
- `soulbound.enchant` - Allows applying the Soulbound enchantment (default: op)

## API

Other plugins can use the Soulbound API to interact with Soulbound items:

```java
// Check if an item has the Soulbound enchantment
boolean hasSoulbound = SoulboundAPI.hasSoulbound(itemStack);

// Add the Soulbound enchantment to an item
ItemStack soulboundItem = SoulboundAPI.addSoulbound(itemStack);

// Remove the Soulbound enchantment from an item
ItemStack normalItem = SoulboundAPI.removeSoulbound(itemStack);

// Get the Soulbound enchantment instance
Enchantment soulboundEnchantment = SoulboundAPI.getSoulboundEnchantment();
```

## Building from Source

1. Clone the repository
2. Build using Gradle:
   ```
   ./gradlew build
   ```
3. The compiled JAR will be in `build/libs/`

## Requirements

- Paper 1.21.x or higher
- Java 17 or higher

## License

This project is in the public domain.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

