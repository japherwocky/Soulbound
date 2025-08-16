# Soulbound - Paper Plugin

Soulbound is a Paper plugin that adds an enchantment for keeping items upon death. This is the Paper implementation for Minecraft 1.21.x.

## Features

- Adds a "Soulbound" enchantment to the game
- Items with this enchantment are kept in the player's inventory upon death
- Configurable chance for the enchantment to be removed after death
- API for other plugins to integrate with Soulbound

## Installation

1. Download the latest release from the [Releases page](https://github.com/japherwocky/Soulbound/releases)
2. Place the JAR file in your server's plugins folder
3. Restart your server
4. Configure the plugin in `plugins/Soulbound/config.yml`

## Configuration

```yaml
# Chance for the Soulbound enchantment to be removed after death (0.0 to 1.0)
# 0.0 = Never removed, 1.0 = Always removed
soulbound-removal-chance: 0.0

# Whether to allow the Soulbound enchantment to be applied to all items
# If false, only items that can normally be enchanted can receive Soulbound
allow-on-all-items: true

# Debug mode - enables additional logging
debug: false
```

## Commands

- `/soulbound` - Shows plugin information
- `/soulbound reload` - Reloads the configuration
- `/soulbound info` - Shows current configuration values

## Permissions

- `soulbound.command` - Allows access to Soulbound commands
- `soulbound.enchant` - Allows applying the Soulbound enchantment

## API for Developers

Soulbound provides an API for other plugins to interact with it:

```java
// Check if an item has the Soulbound enchantment
boolean hasSoulbound = SoulboundAPI.hasSoulbound(itemStack);

// Add the Soulbound enchantment to an item
SoulboundAPI.addSoulbound(itemStack);

// Remove the Soulbound enchantment from an item
SoulboundAPI.removeSoulbound(itemStack);
```

## Building from Source

1. Clone the repository
2. Navigate to the project directory
3. Run `./gradlew build`
4. The built JAR will be in `build/libs/`

## License

This project is licensed under the MIT License - see the LICENSE file for details.

