package dev.elysium.eraces.abilities.abils.attack.aoe

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.targetUtils.ignite
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

@RegisterAbility
@Suppress("unused")
class FireBoomAbility : BaseCooldownAbility(
    id = "fireboom",
    defaultCooldown = "15m",
    comboKey = "6621"
) {
    private var radius: Double = 3.0
    private var fireDuration: String = "10s"

    override fun onActivate(player: Player) {
        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius, useRaycast = true)
            .excludeCaster()
            .execute { it -> it.ignite(TimeParser.parseToTicks(fireDuration).toInt()) }
            .executeEffects(
                EffectsTarget()
                    .from(Executor.PLAYER(player))
                    .particle(Particle.FLAME)
                    .math(
                        RadiusFillBuilder()
                            .circle(radius)
                            .filled(true)
                            .outlineSteps(28)
                            .interpolationFactor(2)
                    )
            )

    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("fireDuration", ::fireDuration)
            read("radius", ::radius)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("fireDuration", fireDuration)
            write("radius", radius)
        }
    }
}