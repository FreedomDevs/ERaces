package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.utils.EffectUtils
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

/**
 * Базовый класс для способностей, которые выдают игроку только эффекты.
 */
abstract class BaseEffectsAbility(
    id: String,
    defaultCooldown: String = "10s",
    defaultEffects: Map<String, EffectData>
) : BaseCooldownAbility(id, defaultCooldown) {

    protected val effectsMap: MutableMap<String, EffectData> = defaultEffects.toMutableMap()

    override fun onActivate(player: Player) {
        EffectUtils.giveEffectsToPlayer(player, effectsMap.values)
        customActivate(player)
    }

    protected open fun customActivate(player: Player) {}

    override fun loadCustomParams(cfg: YamlConfiguration) {
        for ((name, effect) in effectsMap) {
            effect.duration = cfg.getString("${name}_duration", effect.duration)!!
            effect.level = cfg.getInt("${name}_level", effect.level)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        for ((name, effect) in effectsMap) {
            cfg.set("${name}_duration", effect.duration)
            cfg.set("${name}_level", effect.level)
        }
    }

    data class EffectData(var duration: String, var level: Int, val type: PotionEffectType)
}
