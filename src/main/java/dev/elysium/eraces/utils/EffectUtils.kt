package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.updaters.EffectsUpdater
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object EffectUtils {
    @JvmStatic
    fun getPotionEffectType(keyStr: String): PotionEffectType {
        val key = NamespacedKey.fromString(keyStr)
            ?: throw IllegalArgumentException("NamespacedKey is null for $keyStr")

        return Registry.POTION_EFFECT_TYPE[key]
            ?: throw IllegalArgumentException("PotionEffectType not found for key $keyStr")

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
    fun applyEffects(target: LivingEntity, effects: Map<String, Int>, durationTicks: Int) {
        for ((keyStr, level) in effects) {
            try {
                val type = getPotionEffectType(keyStr)
                target.addPotionEffect(PotionEffect(type, durationTicks, level - 1))
            } catch (ex: Exception) {
                ERaces.getInstance().logger.warning(
                    "Ошибка при применении эффекта $keyStr с силой $level на ${target.name}: ${ex.message}"
                )
            }
        }
    }


    @JvmStatic
    fun isLightLevelInRange(block: Block, lightType: String, min: Int, max: Int): Boolean {
        val type = EffectsUpdater.LightType.fromString(lightType)
        val lightLevel = when (type) {
            EffectsUpdater.LightType.SUM -> block.lightLevel
            EffectsUpdater.LightType.SKY -> block.lightFromSky
            EffectsUpdater.LightType.BLOCK -> block.lightFromBlocks
        }
        return lightLevel in min..max
    }

//    Надо для BaseEffectAbility

    @JvmStatic
    fun giveEffectsToPlayer(player: Player, effects: Collection<BaseEffectsAbility.EffectData>) {
        for (effect in effects) {
            try {
                val potionEffect = effectDataToPotionEffect(effect)
                player.addPotionEffect(potionEffect)
            } catch (ex: Exception) {
                ERaces.getInstance().logger.warning(
                    "Ошибка при применении эффекта ${effect.type}: ${ex.message}"
                )
            }
        }
    }

    @JvmStatic
    fun effectDataToPotionEffect(effect: BaseEffectsAbility.EffectData): PotionEffect {
        val ticks = TimeParser.parseToTicks(effect.duration).toInt()
        val type = try {
            getPotionEffectType(effect.type.key.key)
        } catch (_: Exception) {
            effect.type
        }
        return PotionEffect(type, ticks, effect.level - 1, false, true)
    }
}