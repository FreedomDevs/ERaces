package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.SlotType
import dev.elysium.eraces.items.core.Item
import dev.elysium.eraces.items.core.ItemType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot

abstract class Weapon: Item {
    final override val type = ItemType.WEAPON

    open fun onHit(event: EntityDamageByEntityEvent)  {}

    open fun onInventory(playersWithSlots: Map<Player, Set<SlotType>>) {}

    open fun onInteractOffHand(player: Player) {}

    open fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {}
}