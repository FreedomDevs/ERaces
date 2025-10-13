package dev.elysium.eraces.events

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.VisualsManager
import dev.elysium.eraces.gui.core.GuiManager
import dev.elysium.eraces.gui.raceSelect.RaceSelectMenu
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent

object RaceSelectListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val raceId = ERaces.getPlayerMng().getPlayerRaceId(player)

        if (raceId.isNullOrEmpty()) {
            val menu = RaceSelectMenu(player)
            menu.open()
        } else {
            RacesReloader.reloadRaceForPlayer(player)
            VisualsManager.updateVisualsForPlayer(player)
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            val menu = GuiManager.getOpenMenu(player)
            if (menu?.preventClose == true) {
                event.isCancelled = true
            }
        }
    }

//    @EventHandler
//    fun onClick(e: InventoryClickEvent) {
//        val player = e.whoClicked as Player
//        val menu = openMenus[player.name] ?: return
//
//        when (e.view.title) {
//            "Подтверждение выбора" -> menu.handleConfirmClick(e)
//            else -> menu.handleClick(e)
//        }
//
//        val raceId = ERaces.getPlayerMng().getPlayerRaceId(player)
//        if (!raceId.isNullOrEmpty()) {
//            openMenus.remove(player.name)
//            selectingRace.remove(player.name)
//            RacesReloader.reloadRaceForPlayer(player)
//            VisualsManager.updateVisualsForPlayer(player)
//        }
//    }

//    @EventHandler
//    fun onClose(e: InventoryCloseEvent) {
//        val player = e.player as Player
//        if (openMenus.containsKey(player.name)) {
//            ChatUtil.sendAction(player, "<red>Ты должен выбрать расу, прежде чем продолжить!")
//            openMenus[player.name]?.open()
//        }
//    }

}
