package dev.elysium.eraces.listeners

import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.RacesReloader.reloadRaceForPlayer
import dev.elysium.eraces.VisualsManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener : Listener {
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(getInstance(), Runnable {
            reloadRaceForPlayer(player)
            VisualsManager.updateVisualsForPlayer(player)
        }, 5L)
    }
}

