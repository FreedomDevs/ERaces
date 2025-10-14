package dev.elysium.eraces.events

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.gui.core.GuiManager
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Слушатель событий для GUI-меню.
 */
object GuiListener : Listener {
    lateinit var plugin: ERaces

    fun init(plugin: ERaces) {
        this.plugin = plugin
    }

    /**
     * Обрабатывает клики по элементам меню.
     */
    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val menu = GuiManager.getOpenMenu(player) ?: return

        event.isCancelled = true

        menu.handleClick(event)
    }

    /**
     * Убирает меню из менеджера при закрытии инвентаря.
     */
    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val menu = GuiManager.getOpenMenu(player) ?: return

        if (menu.onClose()) {
            menu.closeMessage?.let { ChatUtil.sendAction(player, it) }

            player.server.scheduler.runTaskLater(plugin, Runnable {
                menu.open()
            }, 1L)
        } else {
            GuiManager.close(player)
        }
    }
}