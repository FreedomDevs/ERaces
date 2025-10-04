package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BossRushAbility : IAbility, ICooldownAbility {
    override val id: String = "bossrush"
    private var cooldown: String = "1h"

    private val effectsMap = linkedMapOf(
        "healing" to EffectData("60s", 3, PotionEffectType.HEALTH_BOOST),
        "resistant" to EffectData("2m", 3, PotionEffectType.RESISTANCE),
        "power" to EffectData("2m", 3, PotionEffectType.STRENGTH),
        "regeneration" to EffectData("2m", 2, PotionEffectType.REGENERATION),
        "absorption" to EffectData("5m", 2, PotionEffectType.ABSORPTION),
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "nightvision" to EffectData("20m", 255, PotionEffectType.NIGHT_VISION)
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
