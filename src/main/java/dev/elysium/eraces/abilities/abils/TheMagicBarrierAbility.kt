package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.targetUtils.ignite
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TheMagicBarrierAbility : BaseCooldownAbility(
    id = "themagicbarrier", defaultCooldown = "8m"

) {
    private var radius: Double = 5.0

    private var duration: String = "15s"
    private var level: Int = 3

    override fun activate(player: Player) {
        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius, useRaycast = true)
            .excludeCaster()
            .executeEffects(
                EffectsTarget()
                    .from(Executor.PLAYER(player))
                    .particle(Particle.DUST)
                    .math(
                        RadiusFillBuilder()
                            .circle(radius)
                            .filled(false)
                            .step(0.3)
                            .interpolationFactor(2)
                    )
            )

        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius)
            .excludeCaster()
            .execute { target ->
                if (target is Player) {
                    val effect = PotionEffect(
                        PotionEffectType.ABSORPTION,
                        TimeParser.parseToTicks(duration).toInt(),
                        level,
                        false,
                        false
                    )
                    target.addPotionEffect(effect)

                    Target.from(target)
                        .executeEffects(
                            EffectsTarget()
                                .from(Executor.PLAYER(target))
                                .particle(Particle.DUST)
                                .math(
                                    RadiusFillBuilder()
                                        .sphere(2.0)
                                        .filled(false)
                                        .step(0.2)
                                        .interpolationFactor(2)
                                )
                        )
                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        duration = cfg.getString("duration", duration).toString()
        level = cfg.getInt("level", level)
        radius = cfg.getDouble("radius", radius)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("duration", duration)
        cfg.set("level", level)
        cfg.set("radius", radius)
    }
}