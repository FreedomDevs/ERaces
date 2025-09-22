package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.updaters.EffectsUpdater
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object EffectUtils {
    @JvmStatic
    fun getPotionEffectType(keyStr: String): PotionEffectType {
        val namespacedKeyStr = if (keyStr.contains(":")) keyStr else "minecraft:$keyStr"
        val key = NamespacedKey.fromString(namespacedKeyStr)
            ?: throw IllegalArgumentException("NamespacedKey is null for $namespacedKeyStr")

        return Registry.POTION_EFFECT_TYPE[key]
            ?: throw IllegalArgumentException("PotionEffectType not found for key $namespacedKeyStr")

    }

    @JvmStatic
    fun applyEffects(player: Player, effects: Map<String, Int>, durationTicks: Int) {
        for ((keyStr, level) in effects) {
            try {
                val type = getPotionEffectType(keyStr)
                player.addPotionEffect(PotionEffect(type, durationTicks, level - 1))
            } catch (ex: Exception) {
                ERaces.getInstance().logger.warning(
                    "Ошибка при применении эффекта $keyStr с силой $level: ${ex.message}"
                )
            }
        }
    }

    @JvmStatic
    fun isLightLevelInRange(block: Block, lightType: String, min: Int, max: Int): Boolean {
        val type = EffectsUpdater.LightType.fromString(lightType)
        val lightLevel = when (type) {
            EffectsUpdater.LightType.SUM -> block.lightLevel
            EffectsUpdater.LightType.SKY -> block.getLightFromSky()
            EffectsUpdater.LightType.BLOCK -> block.getLightFromBlocks()
        }
        return lightLevel in min..max
    }
}