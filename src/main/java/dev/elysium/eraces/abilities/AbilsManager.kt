package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.activator.AbilityFactory
import dev.elysium.eraces.abilities.core.utils.AbilityCooldownManager
import dev.elysium.eraces.abilities.core.utils.AbilityScanner
import dev.elysium.eraces.abilities.interfaces.*
import dev.elysium.eraces.exceptions.player.*
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


/**
 * Менеджер всех способностей плагина.
 *
 * Отвечает за регистрацию, хранение, активацию способностей, работу с кулдаунами и комбо.
 * Реализует паттерн Singleton через init().
 */
class AbilsManager private constructor(private val plugin: ERaces) {

    private val context = AbilityContext(plugin)

    companion object {
        @Volatile
        private var instance: AbilsManager? = null

        /**
         * Возвращает текущий экземпляр менеджера способностей.
         * @throws IllegalStateException если init() не был вызван.
         */
        @JvmStatic
        fun getInstance(): AbilsManager =
            instance ?: throw IllegalStateException("AbilsManager не инициализирован! Вызови init() в onEnable()")

        /**
         * Инициализация менеджера. Вызывается один раз при старте плагина.
         */
        @JvmStatic
        @Synchronized
        fun init(plugin: ERaces) {
            if (instance != null) {
                plugin.logger.warning("AbilsManager уже инициализирован, повторная инициализация пропущена.")
                return
            }
            instance = AbilsManager(plugin).apply {
                registerPackage(plugin, "dev.elysium.eraces.abilities.abils")
            }

            plugin.logger.info("AbilsManager успешно инициализирован.")
        }
    }

    fun registerPackage(plugin: JavaPlugin, packageName: String) {
        val factory = AbilityFactory()
        AbilityScanner.scan(plugin, packageName, factory)
            .also { context.registrar.register(*it.toTypedArray()) }
    }


    /**
     * Активация способности игроком.
     * Выполняет проверки:
     * - существует ли способность,
     * - выбран ли рас игрока,
     * - есть ли способность у игрока,
     * - хватает ли маны,
     * - не на кулдауне.
     *
     * Если проверки прошли успешно — активирует способность.
     *
     * @param player игрок
     * @param id ID способности
     *
     * @throws PlayerAbilityNotFoundException если способность не найдена
     * @throws PlayerRaceNotSelectedException если игрок не выбрал расу
     * @throws PlayerAbilityNotOwnedException если игрок не владеет способностью
     * @throws PlayerAbilityOnCooldownException если способность на кулдауне
     */
    fun activate(player: Player, id: String) {
        context.activator.activate(player, id)
    }

    /**
     * Активация способности игроком по комбо-коду.
     *
     * @param player игрок
     * @param combo комбо-код способности
     */
    fun activateByCombo(player: Player, combo: String) {
        context.activator.activateByCombo(player, combo)
    }

    /**
     * Сбрасывает кулдаун конкретной способности игрока.
     *
     * @param player игрок
     * @param abilityId ID способности
     */
    fun clearCooldown(player: Player, abilityId: String) {
        AbilityCooldownManager.resetCooldown(player, abilityId)
    }

    /**
     * Возвращает способность по её ID.
     *
     * @param id ID способности
     * @return экземпляр способности или null, если не зарегистрирована
     */
    fun getAbility(id: String): IAbility? = context.registry.get(id)

    /**
     * Возвращает все зарегистрированные способности.
     *
     * @return список всех способностей
     */
    fun getAllAbilities(): List<IAbility> = context.registry.getAll().toList()
}
