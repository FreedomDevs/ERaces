package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.SlotType
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Material
import org.bukkit.entity.Player

class OneHandedSword(override val plugin: ERaces) : MeleeWeapon(
    id = "one_handed_sword",
    material = Material.IRON_SWORD,
    model = 1004,
    name = "bОдноручный меч",
    damage = 7.0,
    attackSpeed = 1.7,
    maxDurability = 400
) {
    override fun onInventory(playersWithSlots: Map<Player, Set<SlotType>>) {
        for ((player, slots) in playersWithSlots) {
            if(slots.isNotEmpty()) {
                EffectUtils.applyEffects(
                    player,
                    mapOf("minecraft:speed" to 1),
                    TimeParser.parseToTicks("1s").toInt()
                )
            }
        }
    }
}

