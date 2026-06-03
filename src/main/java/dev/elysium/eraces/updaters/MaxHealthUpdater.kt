package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IDisableable
import dev.elysium.eraces.updaters.base.IUpdater
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class MaxHealthUpdater : IUpdater, IDisableable {
    override fun update(race: Race, player: Player) {
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = race.maxHp
    }

    override fun disable(player: Player) {
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = 20.0
    }
}
