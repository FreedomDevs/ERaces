package dev.elysium.eraces.visualUpdaters

import dev.elysium.eraces.datatypes.Race
import org.bukkit.entity.Player

interface IVisualUpdater {
    fun updateVisuals(race: Race, player: Player)

    fun unloadVisuals(player: Player)
}
