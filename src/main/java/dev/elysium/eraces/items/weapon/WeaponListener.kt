package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.core.ItemResolver
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class WeaponListener: Listener {
    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val player = e.damager as? Player ?: return
        val weapon = ItemResolver.resolve(player.inventory.itemInMainHand)
                as? Weapon ?: return

        weapon.onHit(e)
    }
}