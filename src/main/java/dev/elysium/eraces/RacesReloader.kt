package dev.elysium.eraces

import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.items.RaceChangePotion
import dev.elysium.eraces.listeners.*
import dev.elysium.eraces.listeners.PluginMessageListener.Companion.sendAbilities
import dev.elysium.eraces.updaters.UpdatersReloader.disableUpdatersForPlayers
import dev.elysium.eraces.updaters.UpdatersReloader.registerUpdatersListeners
import dev.elysium.eraces.updaters.UpdatersReloader.reloadUpdatersForPlayer
import dev.elysium.eraces.updaters.UpdatersReloader.unloadPlayerDataFromUpdaters
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object RacesReloader : Listener {
    private val listeners: List<Listener> = listOf(
        PlayerJoinListener(),
        PlayerQuitListener(),
        PlayerRespawnListener(),
        RaceChangeGuiListener(),
        RaceChangePotion(),
        PlayerShootBowEventListener(),
    )

    fun reloadRaceForPlayer(player: Player) {
        val race = getInstance().context.playerDataManager.getPlayerRace(player) ?: return

        reloadUpdatersForPlayer(player, race)
        sendAbilities(player)
    }

    fun reloadRaceForAllPlayers() {
        for (i in Bukkit.getOnlinePlayers())
            reloadRaceForPlayer(i)
    }

    fun startListeners(plugin: JavaPlugin) {
        registerUpdatersListeners(plugin)

        for (obj in listeners)
            Bukkit.getPluginManager().registerEvents(obj, plugin)
    }

    fun onPlayerLeave(player: Player) {
        unloadPlayerDataFromUpdaters(player)
        disableUpdatersForPlayers(player)
    }

    fun disableUpdaters() {
        for (i in Bukkit.getOnlinePlayers())
            disableUpdatersForPlayers(i)
    }
}