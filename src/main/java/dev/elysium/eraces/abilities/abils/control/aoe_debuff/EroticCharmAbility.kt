package dev.elysium.eraces.abilities.abils.control.aoe_debuff

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class EroticCharmAbility : BaseEffectsAbility(
    id = "eroticcharm", defaultCooldown = "3m", defaultEffects = linkedMapOf(
        "slowness" to EffectData("5s", 250, PotionEffectType.SLOWNESS)
    )
) {
    private var level: Int = 5
    private var duration: String = "10s"

    override fun onActivate(player: Player) {
        Target.Companion.from(player).excludeCaster().execute { it ->
            EffectUtils.applyEffects(
                it as Player,
                mapOf<String, Int>(
                    "SLOWNESS" to level
                ),
                durationTicks = TimeParser.parseToTicks(duration).toInt()
            )
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) { read("duration", ::duration) }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) { write("duration", duration) }
    }
}