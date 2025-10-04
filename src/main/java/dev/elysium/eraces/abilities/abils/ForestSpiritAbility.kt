package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ForestSpiritAbility : IAbility, ICooldownAbility {
    override val id: String = "forestspirit"
    private var cooldown: String = "10m"

    private val effectsMap = linkedMapOf(
        "regeneration" to EffectData("2m", 2, PotionEffectType.REGENERATION),
        "nightvision" to EffectData("20m", 255, PotionEffectType.NIGHT_VISION),
        "resistant" to EffectData("400s", 1, PotionEffectType.RESISTANCE)
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
