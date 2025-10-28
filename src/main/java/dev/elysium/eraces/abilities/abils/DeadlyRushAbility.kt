package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder

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

        AbilityUtils.runRepeatingForDuration(plugin, duration) {
            Target.from(player)
                .filter(TargetFilter.ENTITIES)
                .inRadius(radius)
                .excludeCaster()
                .execute { target ->
                    target.damage(damagePerSeconds, player)
                }
                .executeEffects(
                    EffectsTarget()
                        .from(Executor.PLAYER(player))
                        .particle(Particle.DAMAGE_INDICATOR)
                        .math(
                            RadiusFillBuilder()
                                .circle(radius)
                                .filled(true)
                                .step(0.3)
                                .interpolationFactor(2)
                        )
                )
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
            read("damagePerSeconds", ::damagePerSeconds)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
            write("damagePerSeconds", damagePerSeconds)
        }
    }
}