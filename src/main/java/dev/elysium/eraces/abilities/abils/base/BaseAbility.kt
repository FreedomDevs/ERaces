package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import org.bukkit.entity.Player

/**
 * Базовый класс для всех способностей игрока.
 *
 * @property id уникальный идентификатор способности
 */
abstract class BaseAbility(override val id: String, val comboKey: String? = null) : BaseAbilityWithConfig(id),
    IComboActivatable {

    /**
     * getComboKey лол че тут написать
     */
    override fun getComboKeyy(): String? = comboKey

    /**
     * Активация способности для указанного игрока.
     * Выполняет [preActivate], затем [onActivate] и [postActivate].
     *
     * @param player игрок, для которого активируется способность
     */
    final override fun activate(player: Player) {
        preActivate(player) {
            triggerActivate(player)
        }
    }

    /**
     * Триггер, который вызывает основную логику активации.
     *
     * @param player игрок, для которого активируется способность
     */
    private fun triggerActivate(player: Player) {
        onActivate(player)
        postActivate(player)
    }

    /**
     * Вызывается перед основной активацией способности.
     * Можно использовать для проверки условий или подготовки.
     * После выполнения своих действий нужно вызвать [next], чтобы продолжить активацию.
     *
     * @param player игрок, для которого активируется способность
     * @param next функция, вызывающая дальнейшую активацию
     */
    protected open fun preActivate(player: Player, next: () -> Unit) {
        next()
    }

    /**
     * Основная логика способности, которая выполняется при активации.
     *
     * @param player игрок, для которого активируется способность
     */
    protected abstract fun onActivate(player: Player)

    /**
     * Вызывается после основной активации способности.
     * Можно использовать для побочных эффектов или очистки.
     *
     * @param player игрок, для которого активируется способность
     */
    protected open fun postActivate(player: Player) {}

    /**
     * Загружает параметры способности из конфигурации.
     *
     * @param cfg YAML конфигурация
     */
    abstract override fun loadParams(cfg: org.bukkit.configuration.file.YamlConfiguration)

    /**
     * Записывает параметры способности по умолчанию в конфигурацию.
     *
     * @param cfg YAML конфигурация
     */
    abstract override fun writeDefaultParams(cfg: org.bukkit.configuration.file.YamlConfiguration)
}