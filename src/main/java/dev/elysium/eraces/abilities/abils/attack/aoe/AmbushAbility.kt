package dev.elysium.eraces.abilities.abils.attack.aoe

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.TimeValue
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

@RegisterAbility
@Suppress("unused")
class AmbushAbility : BaseEffectsAbility(
    id = "ambush", defaultCooldown = "20m",
    defaultEffects = linkedMapOf(
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "jump_bost" to EffectData("20s", 2, PotionEffectType.JUMP_BOOST),
        "nvisibility" to EffectData("20s", 2, PotionEffectType.INVISIBILITY)
    ),
    comboKey = "9323"
) {
    private var duration: TimeValue = TimeValue("20s")
    private var radius: Double = 3.0
    private var blindnessLevel: Int = 2
    private var poisonLevel: Int = 1
    private var nauseaLevel: Int = 1

    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()
        var taskId = 0
        var elapsedTicks = 0

        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (elapsedTicks >= duration.toTicks()) {
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
                        .extra(0.05)
                        .math(
                            builder = RadiusFillBuilder()
                                .circle(radius)
                                .filled(filled = true)
                                .outlineSteps(24)
                                .interpolationFactor(factor = 2)
                        )
                )
                .execute { target ->
                    if (target is Player && ERaces.getInstance().context.playerDataManager.getPlayerRace(target).id != "dark_elf") {
                        target.addPotionEffects(
                            listOf(
                                PotionEffect(
                                    PotionEffectType.BLINDNESS,
                                    duration.toTicksInt(),
                                    blindnessLevel - 1,
                                    false,
                                    false

                                ),
                                PotionEffect(
                                    PotionEffectType.POISON,
                                    duration.toTicksInt(),
                                    poisonLevel - 1,
                                    false,
                                    false

                                ),
                                PotionEffect(
                                    PotionEffectType.NAUSEA,
                                    duration.toTicksInt(),
                                    nauseaLevel - 1,
                                    false,
                                    false

                                )
                            )
                        )
                    }

                }

            elapsedTicks += 20
        }, 0L, 20L).taskId
    }


    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
            read("blindnessLevel", ::blindnessLevel)
            read("poisonLevel", ::poisonLevel)
            read("nauseaLevel", ::nauseaLevel)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
            write("blindnessLevel", blindnessLevel)
            write("poisonLevel", poisonLevel)
            write("nauseaLevel", nauseaLevel)
        }
    }
}