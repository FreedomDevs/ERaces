package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.SlotType
import dev.elysium.eraces.items.core.ItemResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class WeaponInventoryTask(
) : BukkitRunnable() {

    override fun run() {
        val map = mutableMapOf<Weapon, MutableMap<Player, MutableSet<SlotType>>>()

        for (player in Bukkit.getOnlinePlayers()) {
            for ((index, item) in player.inventory.contents.withIndex()) {
                if (item == null || item.type.isAir) continue

                val weapon = ItemResolver.resolve(item) as? Weapon ?: continue

                val slotType = when {
                    item == player.inventory.itemInMainHand -> SlotType.MAINHAND
                    index < 9 -> SlotType.HOTBAR
                    else -> SlotType.INVENTORY
                }

                map.computeIfAbsent(weapon) { mutableMapOf() }
                    .computeIfAbsent(player) { mutableSetOf() }
                    .add(slotType)
            }

            val offhandItem = player.inventory.itemInOffHand
            if (!offhandItem.type.isAir) {
                val weapon = ItemResolver.resolve(offhandItem) as? Weapon
                weapon?.let {
                    map.computeIfAbsent(it) { mutableMapOf() }
                        .computeIfAbsent(player) { mutableSetOf() }
                        .add(SlotType.OFFHAND)
                }
            }
        }


        for ((weapon, playersMap) in map) {
            if (playersMap.isNotEmpty()) {
                weapon.onInventory(playersMap)
            }
        }
    }
}
