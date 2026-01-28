package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.utils.EffectUtils
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

/**
 * Базовый класс для способностей, которые выдают игроку только эффекты.
 *
 * Расширяет [BaseCooldownAbility], поэтому поддерживает кулдаун.
 * Эффекты задаются через карту [effectsMap], где ключ — имя эффекта,
 * а значение — объект [EffectData], содержащий тип, длительность и уровень эффекта.
 *
 * @param id уникальный идентификатор способности
 * @param defaultCooldown кулдаун по умолчанию (строка, например "10s")
 * @param defaultEffects карта эффектов по умолчанию
 */
abstract class BaseEffectsAbility(
    id: String,
    override val name: String? = null,
    override val description: String? = null,
    defaultCooldown: String = "10s",
    defaultEffects: Map<String, EffectData>, comboKey: String? = null
) : BaseCooldownAbility(id, defaultCooldown, comboKey) {

    /** Список эффектов способности, которые будут применяться к игроку */
    protected val effectsMap: MutableMap<String, EffectData> = defaultEffects.toMutableMap()

    /**
     * Активация способности.
     * Выдаёт игроку все эффекты из [effectsMap] и вызывает [customActivate] для
     * дополнительной логики в дочерних классах.
     *
     * @param player игрок, которому применяются эффекты
     */
    override fun onActivate(player: Player) {
        EffectUtils.giveEffectsToPlayer(player, effectsMap.values)
        customActivate(player)
    }

    /**
     * Метод для переопределения дочерними классами,
     * если требуется дополнительная логика при активации.
     *
     * @param player игрок, которому применяется способность
     */
    protected open fun customActivate(player: Player) {}

    /**
     * Загружает пользовательские параметры из конфигурации YAML.
     * Для каждого эффекта читается длительность и уровень.
     *
     * @param cfg YAML конфигурация
     */
    override fun loadCustomParams(cfg: YamlConfiguration) {
        for ((name, effect) in effectsMap) {
            effect.duration = cfg.getString("${name}_duration", effect.duration)!!
            effect.level = cfg.getInt("${name}_level", effect.level)
        }
    }

    /**
     * Записывает пользовательские параметры в конфигурацию YAML.
     * Для каждого эффекта сохраняются длительность и уровень.
     *
     * @param cfg YAML конфигурация
     */
    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        for ((name, effect) in effectsMap) {
            cfg.set("${name}_duration", effect.duration)
            cfg.set("${name}_level", effect.level)
        }
    }

    /**
     * Данные для одного эффекта способности.
     *
     * @property duration длительность эффекта в формате строки (например "10s")
     * @property level уровень эффекта
     * @property type тип эффекта [PotionEffectType]
     */
    data class EffectData(var duration: String, var level: Int, val type: PotionEffectType)
}
