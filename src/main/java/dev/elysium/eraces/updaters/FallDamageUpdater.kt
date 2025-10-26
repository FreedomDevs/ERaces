package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class FallDamageUpdater : Listener{
    @EventHandler
    fun fallDamageEvent(event: EntityDamageEvent) {
        if (event.entity !is Player)
            return

        if (event.cause != EntityDamageEvent.DamageCause.FALL)
            return

        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(event.getEntity() as Player)
        event.setDamage(event.damage * race.fallDamageMultiplier)
    }
}