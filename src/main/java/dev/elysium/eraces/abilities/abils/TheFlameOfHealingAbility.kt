package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TheFlameOfHealingAbility : BaseCooldownAbility(
    id = "theflameofhealing", defaultCooldown = "2m"
) {
    private var radius: Double = 5.0
    private var duration: String = "10s"
    private var level: Int = 3

    override fun onActivate(player: Player) {
        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius)

            .execute { target ->
                if (target is Player) {
                    val effect = PotionEffect(
                        PotionEffectType.REGENERATION,
                        TimeParser.parseToTicks(duration).toInt(),
                        level,
                        false,
                        false
                    )
                    target.addPotionEffect(effect)

                    target.foodLevel = (target.foodLevel - 3.5).toInt().coerceAtLeast(0)
                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radiud", ::radius)
            read("duration", ::duration)
            read("level", ::level)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("duration", duration)
            write("level", level)
        }
    }
}
