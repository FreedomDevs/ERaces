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

    /** Если true, игрок не сможет закрыть меню вручную */
    var preventClose: Boolean = false

    /** Сообщение, которое будет показано при попытке закрытия меню */
    var closeMessage: String? = null

    /** Настройка меню — сюда добавляешь кнопки */
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

    /**
     * Вызывается при закрытии инвентаря.
     * Возвращает true, если меню должно быть снова открыто.
     */
    open fun onClose(): Boolean = preventClose
}

