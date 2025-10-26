package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.events.custom.ManaRegenerationEvent
import dev.elysium.eraces.utils.ConditionLanguageUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ManaRegenerationListener : Listener {

    @EventHandler
    fun onManaRegen(e: ManaRegenerationEvent) {
        val player: Player = e.player.player ?: return
        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(player) ?: return

        val multiplier = getManaRegenMultiplier(race, player)
        e.regenAmount *= multiplier
    }

    private fun getManaRegenMultiplier(race: Race, player: Player): Double {
        var result = 1.0

        for (modifier in race.manaRegenModifiers) {
            if (ConditionLanguageUtils.check(player, modifier.condition)) {
                result *= modifier.multiplier
            }
        }

        return result
    }
}