package dev.elysium.eraces.abilities.abils.special.focus_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FindHimIfYouCanAbility : BaseCooldownAbility(
    id = "findhimifoucan", defaultCooldown = "5m"
) {
    private var duration: String = "5s"
    private var radius: Double = 50.0


    override fun onActivate(player: Player) {
        Target.Companion.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius)
            .excludeCaster()
            .execute { target ->
                if (target is Player) {
                    val effect = PotionEffect(
                        PotionEffectType.GLOWING,
                        TimeParser.parseToTicks(duration).toInt(),
                        0,
                        false,
                        false
                    )
                    target.addPotionEffect(effect)
                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
        }
    }
}