package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * Базовый класс для способностей, которые выдают игроку только эффекты.
 */
abstract class BaseEffectsAbility(
    id: String,
    defaultCooldown: String? = null,
    defaultEffects: Map<String, EffectData>
) : BaseAbility(id), ICooldownAbility {

    private var cooldown: String? = defaultCooldown
    protected val effectsMap: MutableMap<String, EffectData> = defaultEffects.toMutableMap()

    override fun activate(player: Player) {
        val effects = effectsMap.values.map { it.toPotionEffect() }
        player.addPotionEffects(effects)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        if (cfg.contains("cooldown") || cooldown != null) {
            cooldown = cfg.getString("cooldown", cooldown)
        }

        for ((name, effect) in effectsMap) {
            effect.duration = cfg.getString("${name}_duration", effect.duration)!!
            effect.level = cfg.getInt("${name}_level", effect.level)
        }
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        if (cooldown != null) {
            cfg.set("cooldown", cooldown)
        }

        for ((name, effect) in effectsMap) {
            cfg.set("${name}_duration", effect.duration)
            cfg.set("${name}_level", effect.level)
        }
    }

    override fun getCooldown(): Long {
        return cooldown?.let { TimeParser.parseToSeconds(it) } ?: 0L
    }

    data class EffectData(var duration: String, var level: Int, val type: PotionEffectType) {
        fun toPotionEffect(): PotionEffect {
            val ticks = TimeParser.parseToTicks(duration).toInt()
            return PotionEffect(type, ticks, level, false, true)
        }
    }
}
