package dev.elysium.eraces.listeners

import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.RacesReloader.reloadRaceForPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        getInstance().context.specializationsManager.ensurePlayerInitialized(player)
        getInstance().context.manaManager.ensurePlayerInitialized(player)

        Bukkit.getScheduler().runTaskLater(getInstance(), Runnable {
            reloadRaceForPlayer(player)
        }, 5)
    }
}