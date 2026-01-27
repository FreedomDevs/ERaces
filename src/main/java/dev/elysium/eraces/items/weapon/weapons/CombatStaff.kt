package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class CombatStaff(override val plugin: ERaces) : MeleeWeapon(
    id = "combat_staff",
    material = Material.IRON_SWORD,
    name = "<green>Боевой Посох",
    damage = 3.5,
    attackSpeed = 1.4,
    isUnbreakable = false,
    maxDurability = 159,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>3.5",
            "<gray>⚡ <white>Скорость атаки: <yellow>1.4",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
)
