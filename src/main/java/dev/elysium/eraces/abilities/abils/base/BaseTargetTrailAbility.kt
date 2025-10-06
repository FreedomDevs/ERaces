package dev.elysium.eraces.abilities.abils.base

import org.bukkit.Particle
import org.bukkit.entity.Player
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity

/**
 * Базовый класс для способностей, которые используют Target(trail).
 */
abstract class BaseTargetTrailAbility(
    id: String,
    defaultCooldown: String = "10s"
) : BaseCooldownAbility(id, defaultCooldown) {

    protected open var distance: Double = 30.0
    protected open var particle: Particle = Particle.FLAME
    protected open var step: Double = 0.25
    protected open var stopAtBlock: Boolean = true

    protected abstract fun onHitTarget(player: Player, target: LivingEntity)

    protected open val customOnHit: ((LivingEntity) -> Unit)? = null

    override fun activate(player: Player) {
        Target.from(player)
            .filter(*getFilters())
            .trail(
                TargetTrail.Config(
                    distance = distance,
                    step = step,
                    particle = particle,
                    stopAtBlock = stopAtBlock,
                    onHit = customOnHit ?: { target -> onHitTarget(player, target) }
                )
            )
    }

    protected open fun getFilters() = arrayOf(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY)

    override fun loadCustomParams(cfg: YamlConfiguration) {
        distance = cfg.getDouble("distance", distance)
        particle = Particle.valueOf(cfg.getString("particle") ?: particle.name)
        step = cfg.getDouble("step", step)
        stopAtBlock = cfg.getBoolean("stopAtBlock", stopAtBlock)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("distance", distance)
        cfg.set("particle", particle.name)
        cfg.set("step", step)
        cfg.set("stopAtBlock", stopAtBlock)
    }
}

