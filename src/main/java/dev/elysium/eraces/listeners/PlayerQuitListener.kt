package dev.elysium.eraces.listeners

import dev.elysium.eraces.RacesReloader.onPlayerLeave
import dev.elysium.eraces.VisualsManager.unloadVisualsForPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        onPlayerLeave(event.player)
        unloadVisualsForPlayer(event.player)
    }
}
