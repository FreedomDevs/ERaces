package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class IronSword(override val plugin: ERaces) : MeleeWeapon(
    id = "iron_sword",
    material = Material.IRON_SWORD,
    name = "<green>Железный Меч",
    damage = 6.0,
    attackSpeed = 1.6,
    maxDurability = 250,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>6.0",
            "<gray>⚡ <white>Скорость атаки: <yellow>1.6",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
)