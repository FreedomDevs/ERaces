package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material

class CombatStaff(override val plugin: ERaces) : MeleeWeapon(
    id = "combat_staff",
    material = Material.STICK,
    model = 1001,
    name = "Боевой Посох",
    damage = 3.5,
    attackSpeed = 1.4,
    isUnbreakable = false,
    maxDurability = 159
)
