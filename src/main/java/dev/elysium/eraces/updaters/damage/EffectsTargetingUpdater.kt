package dev.elysium.eraces.updaters.damage

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EffectsTargetingUpdater : Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player
        val targetEntity = event.entity as? LivingEntity ?: return
        if (damager == null) return
        val attackerRace = ERaces.getInstance().context.playerDataManager.getPlayerRace(damager)

        if (attackerRace.effectsTargeting.effectsOnPlayerAttacking.isNotEmpty()) {
            EffectUtils.applyEffects(
                targetEntity,
                attackerRace.effectsTargeting.effectsOnPlayerAttacking,
                TimeParser.parseToTicks(attackerRace.effectsTargeting.effectsOnPlayerAttackingTime).toInt()
            )
        }

        if (attackerRace.effectsTargeting.effectsOnPlayerTargeted.isNotEmpty()) {
            EffectUtils.applyEffects(
                damager,
                attackerRace.effectsTargeting.effectsOnPlayerTargeted,
                TimeParser.parseToTicks(attackerRace.effectsTargeting.effectsOnPlayerTargetedTime).toInt()
            )
        }
    }
}
