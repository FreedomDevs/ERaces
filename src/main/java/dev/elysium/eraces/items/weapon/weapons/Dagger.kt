package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class Dagger(override val plugin: ERaces) : MeleeWeapon(
    id = "dagger",
    material = Material.IRON_SWORD,
    name = "<red>Кинжал",
    damage = 5.0,
    attackSpeed = 2.0,
    maxDurability = 200,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>5.0",
            "<gray>⚡ <white>Скорость атаки: <yellow>2.0",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
)