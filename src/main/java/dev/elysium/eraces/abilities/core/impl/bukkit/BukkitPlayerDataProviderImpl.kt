package dev.elysium.eraces.abilities.core.impl.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.providers.IPlayerDataProvider
import dev.elysium.eraces.abilities.core.registry.PlayerRaceData
import org.bukkit.entity.Player

class BukkitPlayerDataProviderImpl : IPlayerDataProvider {
    private val plugin: ERaces = ERaces.getInstance()

    override fun getPlayerRace(player: Player): PlayerRaceData? {
        val race = plugin.context.playerDataManager.getPlayerRace(player) ?: return null
        return PlayerRaceData(
            raceId = race.id.toString(),
            abilities = race.abilities.toSet()
        )
    }
}
