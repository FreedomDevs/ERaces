package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class Dagger(override val plugin: ERaces) : MeleeWeapon(
    id = "dagger",
    material = Material.IRON_SWORD,
    model = 1002,
    name = "Кинжал",
    damage = 5.0,
    attackSpeed = 2.0,
    maxDurability = 200
)