package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent

class NeutralMobsUpdater : Listener {
    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val target = event.target
        if (target !is Player) return
        val race = ERaces.getPlayerMng().getPlayerRace(target)

        val neutralMobNames = race.neutralMobs

        for (mobName in neutralMobNames) {
            try {
                val mobType = EntityType.valueOf(mobName.uppercase())
                if (mobType == event.entityType) {
                    event.isCancelled = true
                    return
                }
            } catch (e: IllegalArgumentException) {
                continue
            }
        }
    }

}