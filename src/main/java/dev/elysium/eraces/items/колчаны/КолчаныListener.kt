package dev.elysium.eraces.items.колчаны

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.gui.GuiHolder
import dev.elysium.eraces.gui.GuiType
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class КолчаныListener: Listener {

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        val clickType = actionToOnClickType(e.action) ?: return
        val player = e.player

        if (clickType != ClickType.LEFT) return
        val itemInMainHand = ItemResolver.resolve(player.inventory.itemInMainHand) as? Колчаны ?: return

        itemInMainHand.openКолчаныGui(player)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = e.inventory.holder as? Player ?: return
        val itemInMainHand = ItemResolver.resolve(player.inventory.itemInMainHand) as? Колчаны ?: return
        val holder = e.view.topInventory.holder

        if (holder is GuiHolder && holder.guiType == GuiType.QUIVER && holder.id == itemInMainHand.name) {
            if (e.slot >= itemInMainHand.arrowSlots) {
                e.isCancelled = true
                return
            }

            val item = e.cursor

            if (item.type.isAir) return
            if (item.type != Material.ARROW) {
                e.isCancelled = true
                return
            }

            val size = e.inventory.size

            object : BukkitRunnable() {
                override fun run() {
                    var count = 0

                    for (i in 0 until size) {

                        val item = e.inventory.getItem(i) ?: continue
                        if (item.type != Material.ARROW) continue

                        count += item.amount
                    }

                    ItemState(player.inventory.itemInMainHand).setInt(StateKeys.ARROWS_КОЛЧАН, count)
                }
            }.runTaskLater(ERaces.getInstance(), 1L)

        }
    }

//    HELPERS

    private fun actionToOnClickType(action: Action): ClickType? {
        val clickType: ClickType? = when (action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> ClickType.LEFT
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> ClickType.RIGHT
            else -> null
        }

        return clickType
    }
}