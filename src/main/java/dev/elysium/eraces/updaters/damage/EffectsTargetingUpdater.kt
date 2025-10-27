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

        if (damager != null) {
            val attackerRace = ERaces.getInstance().context.playerDataManager.getPlayerRace(damager)
            val effectsOnAttacking = attackerRace.effectsTargeting.effectsOnPlayerAttacking
            val effectsOnAttackingTime = attackerRace.effectsTargeting.effectsOnPlayerAttackingTime

            if (effectsOnAttacking.isNotEmpty()) {
                EffectUtils.applyEffects(
                    targetEntity,
                    effectsOnAttacking,
                    TimeParser.parseToTicks(effectsOnAttackingTime).toInt()
                )
            }
        }

        val targetRace = ERaces.getInstance().context.playerDataManager.getPlayerRace(targetEntity as Player)
        val effectsOnTargeted = targetRace.effectsTargeting.effectsOnPlayerTargeted
        val effectsOnTargetedTime = targetRace.effectsTargeting.effectsOnPlayerTargetedTime

        if (effectsOnTargeted.isNotEmpty()) {
            EffectUtils.applyEffects(
                targetEntity,
                effectsOnTargeted,
                TimeParser.parseToTicks(effectsOnTargetedTime).toInt()
            )
        }
    }
}
