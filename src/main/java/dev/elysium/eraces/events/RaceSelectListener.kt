package dev.elysium.eraces.events

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.VisualsManager
import dev.elysium.eraces.gui.RaceSelectMenu
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent

object RaceSelectListener : Listener {
    private val openMenus = mutableMapOf<String, RaceSelectMenu>()
    private val selectingRace = mutableSetOf<String>()

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        val raceId = ERaces.getPlayerMng().getPlayerRaceId(player)

        if ((raceId == null || raceId.isEmpty()) && !openMenus.containsKey(player.name)) {
            val menu = RaceSelectMenu(player)
            openMenus[player.name] = menu
            if (!selectingRace.contains(player.name)) {
                selectingRace.add(player.name)
            }
            menu.open()
        } else if (raceId != null && raceId.isNotEmpty()) {
            RacesReloader.reloadRaceForPlayer(player)
            VisualsManager.updateVisualsForPlayer(player)
        }
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val menu = openMenus[player.name] ?: return
        menu.handleClick(e)

        val raceId = ERaces.getPlayerMng().getPlayerRaceId(player)
        if (raceId != null && raceId.isNotEmpty()) {
            openMenus.remove(player.name)
            selectingRace.remove(player.name)
            RacesReloader.reloadRaceForPlayer(player)
            VisualsManager.updateVisualsForPlayer(player)
        }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as Player
        if (openMenus.containsKey(player.name)) {
            ChatUtil.sendAction(player, "<red>Ты должен выбрать расу, прежде чем продолжить!")
            openMenus[player.name]?.open()
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        val player = e.entity
        if (player is Player && selectingRace.contains(player.name)) {
            e.isCancelled = true
        }
    }

}

