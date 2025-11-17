package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.abilities.AbilsManager
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

/**
 * Базовый класс для способностей с поддержкой кулдаунов.
 *
 * Расширяет [BaseAbility] и реализует [ICooldownAbility].
 * Позволяет задавать дефолтное значение кулдауна, а также
 * загружать/сохранять его в конфигурацию YAML.
 *
 * @property defaultCooldown значение кулдауна по умолчанию (строка, например "10s")
 */
abstract class BaseCooldownAbility(
    id: String,
    private val defaultCooldown: String = "10s"
) : BaseAbility(id), ICooldownAbility {

    /** Текущее значение кулдауна, загруженное из конфигурации */
    private var cooldown: String = defaultCooldown

    /**
     * Загружает параметры способности из конфигурации.
     * В том числе устанавливает значение кулдауна из поля "cooldown".
     *
     * @param cfg YAML конфигурация
     */
    final override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getString("cooldown", defaultCooldown)!!
        loadCustomParams(cfg)
    }

    /**
     * Записывает параметры способности по умолчанию в конфигурацию.
     * В том числе сохраняет значение кулдауна.
     *
     * @param cfg YAML конфигурация
     */
    final override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", cooldown)
        writeCustomDefaults(cfg)
    }

    /**
     * Позволяет дочерним классам загружать свои кастомные параметры из конфигурации.
     *
     * @param cfg YAML конфигурация
     */
    protected open fun loadCustomParams(cfg: YamlConfiguration) {}

    /**
     * Позволяет дочерним классам записывать свои кастомные параметры по умолчанию в конфигурацию.
     *
     * @param cfg YAML конфигурация
     */
    protected open fun writeCustomDefaults(cfg: YamlConfiguration) {}

    /**
     * Сбрасывает кулдаун для указанного игрока, чтобы способность могла быть использована снова.
     *
     * @param player игрок, для которого сбрасывается кулдаун
     */
    protected fun resetCooldown(player: Player) {
        AbilsManager.getInstance().clearCooldown(player, id)
    }

    /**
     * Возвращает текущий кулдаун способности в секундах.
     *
     * @return кулдаун в секундах
     */
    override fun getCooldown(): Long = TimeParser.parseToSecondsDouble(cooldown).toLong()
}