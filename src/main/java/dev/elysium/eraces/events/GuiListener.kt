package dev.elysium.eraces.events

import dev.elysium.eraces.gui.core.GuiManager
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Слушатель событий для GUI-меню.
 */
object GuiListener : Listener {

    /**
     * Обрабатывает клики по элементам меню.
     */
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val menu = GuiManager.getOpenMenu(player) ?: return
        menu.handleClick(event)
    }

    /**
     * Убирает меню из менеджера при закрытии инвентаря.
     */
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val menu = GuiManager.getOpenMenu(player) ?: return

        if (menu.onClose()) {
            menu.closeMessage?.let { ChatUtil.sendAction(player, it) }
            menu.open()
        } else {
            GuiManager.close(player)
        }
    }
}