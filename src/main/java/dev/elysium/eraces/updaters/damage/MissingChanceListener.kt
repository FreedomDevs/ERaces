package dev.elysium.eraces.updaters.damage

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class MissingChanceListener: Listener {

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        if (event.damager  !is Player) return
        val player: Player = event.damager as Player
        val race: Race = ERaces.getInstance().context.playerDataManager.getPlayerRace(player)

        if (race.missingChance > 0.0) {
            if (Random.nextDouble() < race.missingChance) {
                event.isCancelled = true
                val chance = race.missingChance * 100
                ChatUtil.message(player, "<red>Вы промахнклись! <orange>Шанс промаха составляет: $chance%")
            }
        }
    }
}