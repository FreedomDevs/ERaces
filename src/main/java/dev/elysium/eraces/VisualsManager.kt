package dev.elysium.eraces

import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.visualUpdaters.EarsUpdater
import dev.elysium.eraces.visualUpdaters.IVisualUpdater
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object VisualsManager {
    private val visualUpdaters = listOf<IVisualUpdater>(
        EarsUpdater(),
    ) as MutableList<IVisualUpdater>

    @JvmStatic
    fun updateVisualsForPlayer(player: Player) {
        val race = getInstance().context.playerDataManager.getPlayerRace(player)

        for (updater in visualUpdaters) updater.updateVisuals(race, player)
    }

    fun reloadVisualsForAllPlayer() {
        for (player in Bukkit.getOnlinePlayers()) updateVisualsForPlayer(player)
    }

    fun unloadVisualsForPlayer(player: Player) {
        for (updater in visualUpdaters) updater.unloadVisuals(player)
    }
}