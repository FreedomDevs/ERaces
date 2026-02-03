package dev.elysium.eraces.items.weapon

import org.bukkit.event.block.Action
import dev.elysium.eraces.items.core.ItemResolver
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class WeaponListener : Listener {
    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val player = e.damager as? Player ?: return
        val (weapon, _) = getWeaponToMainOrOffHand(player, Hand.MAIN_HAND) ?: return

        weapon.onHit(e)
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        val clickType = actionToOnClickType(e.action) ?: return
        val slot = e.hand ?: return

        val hand = when (slot) {
            EquipmentSlot.HAND -> Hand.MAIN_HAND
            EquipmentSlot.OFF_HAND -> Hand.OFF_HAND
            else -> return
        }

        val (weapon, _) = getWeaponToMainOrOffHand(e.player, hand) ?: return

        weapon.onInteract(e.player, slot, clickType)
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return

        val (weapon, _) = getWeaponToMainOrOffHand(player, Hand.ALL) ?: return

        weapon.onDamage(e)
    }

    @EventHandler
    fun onKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return

        val (weapon, _) = getWeaponToMainOrOffHand(killer, Hand.MAIN_HAND) ?: return

        weapon.onKill(e, killer)
    }

    /*
    * HELPERS
    */

    private fun actionToOnClickType(action: Action): ClickType? {
        val clickType: ClickType? = when (action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> ClickType.LEFT
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> ClickType.RIGHT
            else -> null
        }

        return clickType
    }

    private fun getWeaponToMainOrOffHand(player: Player, hand: Hand = Hand.ALL): Pair<Weapon, Hand>? {
        val inv = player.inventory

        fun main(): Pair<Weapon, Hand>? {
            val w = ItemResolver.resolve(inv.itemInMainHand) as? Weapon ?: return null
            return Pair(w, Hand.MAIN_HAND)
        }

        fun off(): Pair<Weapon, Hand>? {
            val w = ItemResolver.resolve(inv.itemInOffHand) as? Weapon ?: return null
            return Pair(w, Hand.OFF_HAND)
        }

        return when (hand) {
            Hand.ALL -> main() ?: off()
            Hand.MAIN_HAND -> main()
            Hand.OFF_HAND -> off()
        }
    }

    private enum class Hand {
        MAIN_HAND,
        OFF_HAND,
        ALL
    }
}