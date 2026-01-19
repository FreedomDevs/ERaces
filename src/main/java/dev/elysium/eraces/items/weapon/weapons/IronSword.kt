package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class IronSword(override val plugin: ERaces) : MeleeWeapon(
    id = "iron_sword",
    material = Material.IRON_SWORD,
    model = 1003,
    name = "Железный Меч",
    damage = 6.0,
    attackSpeed = 1.6,
    maxDurability = 250
)