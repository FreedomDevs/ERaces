package dev.elysium.eraces.gui.core

import dev.elysium.eraces.ERaces
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
    var isReopened: Boolean = false
    var closeMessage: String? = null

    abstract fun setup()

    fun setButton(slot: Int, button: GuiButton) {
        inv.setItem(slot, button.item)
        buttons[slot] = button
    }

    fun open(isnotreopened: Boolean = false) {
        setup()

        if (GuiManager.getOpenMenu(player) != null && !isnotreopened) {
            isReopened = true
        }

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
