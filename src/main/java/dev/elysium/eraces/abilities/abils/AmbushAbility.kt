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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AmbushAbility : BaseEffectsAbility(
    id = "ambush", defaultCooldown = "20m",
    defaultEffects = linkedMapOf(
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "jump_bost" to EffectData("20s", 2, PotionEffectType.JUMP_BOOST),
        "nvisibility" to EffectData("20s", 2, PotionEffectType.INVISIBILITY)
    )
) {
    private var duration: String = "20s"
    private var radius: Double = 3.0
    private var blindnessLevel: Int = 2
    private var poisionLevel: Int = 1
    private var nauseaLevel: Int = 1

    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()
        var taskId = 0
        val elapsedSeconds = 0

        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (elapsedSeconds >= TimeParser.parseToSecondsDouble(duration).toInt()) {
                Bukkit.getScheduler().cancelTask(taskId)
                return@Runnable
            }
            Target.from(player)
                .filter(TargetFilter.ENTITIES)
                .inRadius(radius)
                .excludeCaster()
                .executeEffects(
                    EffectsTarget()
                        .from(Executor.PLAYER(entity = player))
                        .particle(p = Particle.SMOKE)
                        .math(
                            builder = RadiusFillBuilder()
                                .circle(radius)
                                .filled(filled = true)
                                .step(step = 0.3)
                                .interpolationFactor(factor = 2)
                        )
                )
                .execute { target ->
                    if (target is Player && ERaces.getInstance().context.playerDataManager.getPlayerRace(target).id != "dark_elf") {
                        target.addPotionEffects(
                            listOf(
                                PotionEffect(
                                    PotionEffectType.BLINDNESS,
                                    TimeParser.parseToTicks(duration).toInt(),
                                    blindnessLevel - 1,
                                    false,
                                    false

                                ),
                                PotionEffect(
                                    PotionEffectType.POISON,
                                    TimeParser.parseToTicks(duration).toInt(),
                                    poisionLevel - 1,
                                    false,
                                    false

                                ),
                                PotionEffect(
                                    PotionEffectType.NAUSEA,
                                    TimeParser.parseToTicks(duration).toInt(),
                                    nauseaLevel - 1,
                                    false,
                                    false

                                )
                            )
                        )
                    }

                }
        }, 0L, 20L).taskId
    }


    override fun loadCustomParams(cfg: YamlConfiguration) {
        duration = cfg.getString("duration", duration).toString()
        radius = cfg.getDouble("radius", radius)
        blindnessLevel = cfg.getInt("blindnessLevel", blindnessLevel)
        poisionLevel = cfg.getInt("poisionLevel", poisionLevel)
        nauseaLevel = cfg.getInt("nauseaLevel", nauseaLevel)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("duration", duration)
        cfg.set("radius", radius)
        cfg.set("blindnessLevel", blindnessLevel)
        cfg.set("poisionLevel", poisionLevel)
        cfg.set("nauseaLevel", nauseaLevel)
    }
}