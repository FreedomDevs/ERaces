package dev.elysium.eraces.abilities.abils.control.aoe_debuff

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class AncientKnowledgeAbility: BaseCooldownAbility(
    id = "ancientknowledge", defaultCooldown = "3m"
) {
    private var radius: Double = 15.0
    private var duration: String = "15s"

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
        ConfigHelper.with(cfg){
            read("radius", ::radius)
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg){
            write("radius", radius)
            write("duration", duration)
        }
    }
}