package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.sql.Time

class RageModeAbility : IAbility, ICooldownAbility {
    override val id: String = "ragemode"
    private var cooldown: String = "30m"

    private val effectsMap = linkedMapOf(
        "resistant" to EffectData("1m", 3, PotionEffectType.RESISTANCE),
        "power" to EffectData("2m", 3, PotionEffectType.STRENGTH),
        "regeneration" to EffectData("1m", 2, PotionEffectType.REGENERATION),
        "absorption" to EffectData("5m", 1, PotionEffectType.ABSORPTION)
    )

    override fun activate(player: Player) {
        val effects = effectsMap.values.map { it.toPotionEffect() }
        player.addPotionEffects(effects)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getString("cooldown", cooldown)!!

        for ((name, effect) in effectsMap) {
            effect.duration = cfg.getString("${name}_duration", effect.duration)!!
            effect.level = cfg.getInt("${name}_level", effect.level)
        }
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", cooldown)

        for ((name, effect) in effectsMap) {
            cfg.set("${name}_duration", effect.duration)
            cfg.set("${name}_level", effect.level)
        }
    }

    override fun getCooldown(): Long {
        return TimeParser.parseToSeconds(cooldown)
    }

    data class EffectData(var duration: String, var level: Int, val type: PotionEffectType) {
        fun toPotionEffect(): PotionEffect {
            val ticks = TimeParser.parseToTicks(duration).toInt()
            return PotionEffect(type, ticks, level, false, true)
        }
    }
}
