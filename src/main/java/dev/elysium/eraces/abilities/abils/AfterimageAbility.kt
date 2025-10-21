package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import org.bukkit.Bukkit
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


    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()
        var taskId = 0
        var functia = 0

        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (functia >= TimeParser.parseToSeconds(duration)) {
                Bukkit.getScheduler().cancelTask(taskId)
                return@Runnable
            }

            Target.from(player = player)
                .executeEffects(
                    EffectsTarget()
                        .from(Executor.PLAYER(entity = player))
                        .particle(p = Particle.CAMPFIRE_COSY_SMOKE)
                        .math(builder = RadiusFillBuilder().sphere(radius = radius).filled(true).step(0.1))
                )

            functia++
        }, 0L, 20L).taskId
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        duration = cfg.getString("taskId", duration).toString()
        radius = cfg.getDouble("radius", radius)

    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("duration", duration)
        cfg.set("radius", radius)

    }
}