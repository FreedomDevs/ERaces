package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class DeadlyRushAbility : BaseEffectsAbility(
    id = "deadlyrush", defaultCooldown = "30m",

    defaultEffects = linkedMapOf(
        "speed" to EffectData("30s", 4, PotionEffectType.SPEED),
        "invisibility" to EffectData("30s", 255, PotionEffectType.INVISIBILITY)
    )

) {
    private var radius: Double = 3.0
    private var duration: String = "30s"
    private var damagePerSeconds: Double = 1.0

    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()
        var taskId = 0
        var elapsedSeconds = 0

        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (elapsedSeconds >= TimeParser.parseToSeconds(duration)) {
                Bukkit.getScheduler().cancelTask(taskId)
                return@Runnable
            }
            Target.from(player)
                .filter(TargetFilter.ENTITIES)
                .inRadius(radius)
                .excludeCaster()
                .execute { target ->
                    target.damage(damagePerSeconds, player)
                }
                .executeEffects(
                    EffectsTarget()
                        .from(Executor.PLAYER(entity = player))
                        .particle(p = Particle.DAMAGE_INDICATOR)
                        .math(
                            builder = RadiusFillBuilder()
                                .circle(radius)
                                .filled(filled = true)
                                .step(step = 0.3)
                                .interpolationFactor(factor = 2)
                        )
                )

            elapsedSeconds++
        }, 0L, 20L).taskId
    }


    override fun loadCustomParams(cfg: YamlConfiguration) {
        duration = cfg.getString("duration", duration).toString()
        radius = cfg.getDouble("radius", radius)
        damagePerSeconds = cfg.getDouble("damagePerSeconds", damagePerSeconds)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("duration", duration)
        cfg.set("radius", radius)
        cfg.set("damagePerSeconds", damagePerSeconds)
    }
}