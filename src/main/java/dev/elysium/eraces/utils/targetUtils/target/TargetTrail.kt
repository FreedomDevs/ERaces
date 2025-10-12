package dev.elysium.eraces.utils.targetUtils.target

import dev.elysium.eraces.utils.vectors.Vec3
import dev.elysium.eraces.utils.vectors.Vec3Utils
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity

/**
 * Визуализация луча
 */
object TargetTrail {

    data class Config(
        val distance: Double = 30.0,
        val step: Double = 0.3,
        val particle: Particle? = null,
        val stopAtBlock: Boolean = true,
        val onStep: ((Vec3) -> Unit)? = null,
        val onHit: ((LivingEntity) -> Unit)? = null
    )

    fun trail(caster: LivingEntity, config: Config) {
        val origin = Vec3(caster.location.x, caster.location.y + caster.eyeHeight, caster.location.z)
        val direction = Vec3Utils.fromPlayerLook(caster)
        val hits = TargetRaycast.raycast(origin, direction, config.distance, config.step, caster.world)

        for (hit in hits) {
            spawnParticle(caster, hit, config.particle)
            config.onStep?.invoke(hit.position)
            hit.hitEntities.forEach { config.onHit?.invoke(it) }

            if (shouldStop(hit, config.stopAtBlock)) break
        }
    }

    // ----------------- helpers -----------------

    private fun spawnParticle(caster: LivingEntity, hit: RaycastHit, particle: Particle?) {
        if (particle == null) return
        caster.world.spawnParticle(
            particle,
            hit.position.x,
            hit.position.y,
            hit.position.z,
            1
        )
    }

    private fun shouldStop(hit: RaycastHit, stopAtBlock: Boolean): Boolean =
        stopAtBlock && hit.hitBlock != null || hit.hitEntities.isNotEmpty()
}
