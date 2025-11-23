package dev.elysium.eraces.abilities.core.impl.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.providers.IPlayerDataProvider
import dev.elysium.eraces.abilities.core.registry.PlayerRaceData
import org.bukkit.entity.Player

class BukkitPlayerDataProviderImpl : IPlayerDataProvider {
    override fun getPlayerRace(player: Player): PlayerRaceData? =
        ERaces.getInstance().context.playerDataManager.getPlayerRace(player)?.let { race ->
            PlayerRaceData(
                raceId = race.id.toString(),
                abilities = race.abilities.toSet()
            )
        }
}
