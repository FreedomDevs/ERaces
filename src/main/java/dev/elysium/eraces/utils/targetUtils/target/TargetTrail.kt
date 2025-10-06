package dev.elysium.eraces.utils.targetUtils.target

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Визуализация луча
 */
object TargetTrail {
    data class Config(
        val distance: Double = 30.0,
        val step: Double = 0.3,
        val particle: Particle? = null,
        val stopAtBlock: Boolean = true,
        val onStep: (Location) -> Unit = {},
        val onHit: (LivingEntity) -> Unit = {}
    )

    fun cast(player: Player, config: Config) {
        val loc = player.eyeLocation.clone()
        val dir = loc.direction.normalize()
        val iterations = (config.distance / config.step).toInt()
        for (i in 0..iterations) {
            loc.add(dir.clone().multiply(config.step))
            config.particle?.let {
                player.world.spawnParticle(it, loc, 2, 0.02, 0.02, 0.02, 0.0)
            }
            config.onStep(loc)
            if (config.stopAtBlock && loc.block.type.isSolid) break
        }
    }
}