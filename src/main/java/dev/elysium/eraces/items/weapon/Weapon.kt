package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.SlotType
import dev.elysium.eraces.items.core.Item
import dev.elysium.eraces.items.core.ItemType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

abstract class Weapon: Item {
    final override val type = ItemType.WEAPON

    open fun onHit(event: EntityDamageByEntityEvent)  {}

    open fun onInventory(playersWithSlots: Map<Player, Set<SlotType>>) {}
}