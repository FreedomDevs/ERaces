package dev.elysium.eraces.listeners

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.gui.core.GuiManager
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent

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

        if (menu.isReopened) {
            menu.isReopened = false
            return
        }

        if (menu.preventClose) {
            menu.closeMessage?.let { player.actionMsg(it) }

            player.server.scheduler.runTaskLater(plugin, Runnable {
                menu.open(true)
            }, 1L)
        } else {
            GuiManager.close(player)
        }
    }

    /**
     * Закрывает GUI после выхода игрока с сервера
     */
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        GuiManager.close(player)
    }
}