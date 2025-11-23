package dev.elysium.eraces.abilities.core.interfaces.providers

import dev.elysium.eraces.abilities.core.registry.PlayerRaceData
import org.bukkit.entity.Player

interface IPlayerDataProvider {
    fun getPlayerRace(player: Player): PlayerRaceData?
}