package dev.elysium.eraces.gui.core

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

/**
 * Базовый класс GUI-меню.
 */
abstract class GuiBase(
    val player: Player,
    title: String,
    size: Int = 54
) {
    val inv: Inventory = Bukkit.createInventory(null, size, Component.text(title))
    private val buttons = mutableMapOf<Int, GuiButton>()

    var preventClose: Boolean = false
    var closeMessage: String? = null

    internal var programmaticOpen: Boolean = false

    abstract fun setup()

    fun setButton(slot: Int, button: GuiButton) {
        inv.setItem(slot, button.item)
        buttons[slot] = button
    }

    fun open() {
        setup()
        player.openInventory(inv)
        GuiManager.setOpenMenu(player, this)
    }

    open fun handleClick(event: InventoryClickEvent) {
        event.isCancelled = true
        buttons[event.slot]?.onClick?.invoke(event)
    }

    fun clearButtons() {
        buttons.clear()
        inv.clear()
    }
}
