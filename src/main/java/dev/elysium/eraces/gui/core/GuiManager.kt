package dev.elysium.eraces.gui.core

import org.bukkit.entity.Player

/**
 * Отслеживает открытые меню игроков.
 */
object GuiManager {

    private val openMenus = mutableMapOf<String, GuiBase>()

    fun getOpenMenu(player: Player): GuiBase? = openMenus[player.name]

    fun setOpenMenu(player: Player, menu: GuiBase) {
        openMenus[player.name] = menu
    }

    fun close(player: Player) {
        openMenus.remove(player.name)
    }

    internal val allOpen: Map<String, GuiBase> get() = openMenus
}
