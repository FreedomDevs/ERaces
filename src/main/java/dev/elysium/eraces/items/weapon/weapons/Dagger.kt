package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class Dagger(override val plugin: ERaces) : MeleeWeapon(
    id = "dagger",
    material = Material.IRON_SWORD,
    name = "<green>Кинжал",
    damage = 5.0,
    attackSpeed = 2.0,
    maxDurability = 200,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 5.0,
            attackSpeed = 2.0
        )
    )
)