package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CombatStaff : MeleeWeapon(
    id = "combat_staff",
    material = Material.IRON_SWORD,
    name = "<green>Боевой Посох",
    damage = 3.5,
    attackSpeed = 1.4,
    isUnbreakable = false,
    maxDurability = 159,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 3.5,
            attackSpeed = 1.4
        )
    )
)