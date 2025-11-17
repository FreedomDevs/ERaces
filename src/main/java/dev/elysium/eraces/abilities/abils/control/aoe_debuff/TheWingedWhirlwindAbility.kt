package dev.elysium.eraces.abilities.abils.control.aoe_debuff

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TheWingedWhirlwindAbility : BaseCooldownAbility(
    id = "thewingedwhirlwind", defaultCooldown = "2m"
) {
    private var witherDuration: String = "4s"
    private var nauseDuration: String = "3s"
    private var slownessDuration: String = "5s"
    private var radius: Double = 40.0
    private var witherLevel: Int = 1
    private var nauseLevel: Int = 1
    private var slownessLevel: Int = 3

    override fun onActivate(player: Player) {
        Target.Companion.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius)
            .excludeCaster()
            .execute { target ->
                if (target is Player) {
                    target.addPotionEffects(
                        listOf(
                            PotionEffect(
                                PotionEffectType.WITHER,
                                TimeParser.parseToTicks(witherDuration).toInt(),
                                witherLevel - 1,
                                false,
                                false

                            ),
                            PotionEffect(
                                PotionEffectType.NAUSEA,
                                TimeParser.parseToTicks(nauseDuration).toInt(),
                                nauseLevel - 1,
                                false,
                                false

                            ),
                            PotionEffect(
                                PotionEffectType.SLOWNESS,
                                TimeParser.parseToTicks(slownessDuration).toInt(),
                                slownessLevel,
                                false,
                                false
                            )
                        )
                    )
                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("witherDuration", ::witherDuration)
            read("nauseDuration", ::nauseDuration)
            read("slownessDuration", ::slownessDuration)
            read("witherLevel", ::witherLevel)
            read("nauseLevel", ::nauseLevel)
            read("slownessLevel", ::slownessLevel)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("witherDuration", witherDuration)
            write("nauseDuration", nauseDuration)
            write("slownessDuration", slownessDuration)
            write("witherLevel", witherLevel)
            write("nauseLevel", nauseLevel)
            write("slownessLevel", slownessLevel)
        }
    }
}