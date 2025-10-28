package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class AfterimageAbility : BaseEffectsAbility(
    id = "afterimage", defaultCooldown = "10m",

    defaultEffects = linkedMapOf(
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "strength" to EffectData("20m", 255, PotionEffectType.STRENGTH),
        "nightvision" to EffectData("400s", 1, PotionEffectType.NIGHT_VISION),
        "invisibility" to EffectData("30s", 255, PotionEffectType.INVISIBILITY)
    )
) {
    private var duration: String = "30s"
    private var radius: Double = 2.0

    override fun onActivate(player: Player) {
        super.onActivate(player)

        val plugin = ERaces.getInstance()

        AbilityUtils.runRepeatingForDuration(plugin, duration) {
            Target.from(player)
                .executeEffects(
                    EffectsTarget()
                        .from(Executor.PLAYER(player))
                        .particle(Particle.CAMPFIRE_COSY_SMOKE)
                        .math(
                            RadiusFillBuilder()
                                .sphere(radius)
                                .filled(true)
                                .step(0.1)
                        )
                )
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("taskId", ::duration)
            read("radius", ::radius)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("taskId", duration)
            write("radius", radius)
        }
    }
}