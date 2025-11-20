package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.interfaces.*
import dev.elysium.eraces.exceptions.ExceptionProcessor
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.exceptions.internal.*
import dev.elysium.eraces.exceptions.player.*
import dev.elysium.eraces.utils.actionMsg
import io.github.classgraph.ClassGraph
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.createInstance

/**
 * Менеджер всех способностей плагина.
 *
 * Отвечает за:
 * - регистрацию способностей,
 * - хранение всех экземпляров способностей,
 * - активацию способностей игроками с проверкой прав, маны и кулдаунов,
 * - сброс кулдаунов и работу с комбо-способностями.
 *
 * Использует паттерн Singleton: создаётся один раз через [init].
 *
 * @property plugin основной плагин [ERaces]
 */
class AbilsManager private constructor(private val plugin: ERaces) {

    /** Все зарегистрированные способности по их уникальному ID */
    private val abilities: MutableMap<String, IAbility> = mutableMapOf()

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

            instance = AbilsManager(plugin).apply { registerPackage(plugin, "dev.elysium.eraces.abilities.abils") }
            plugin.logger.info("AbilsManager успешно инициализирован.")
        }
    }

    fun registerPackage(plugin: JavaPlugin, packageName: String) {
        val scanResult = ClassGraph()
            .enableAllInfo()
            .acceptPackages(packageName)
            .addClassLoader(plugin.javaClass.classLoader)
            .scan()

        val abils: MutableList<IAbility> = mutableListOf()

        for (clsInfo in scanResult.getClassesWithAnnotation(RegisterAbility::class.java.name)) {
            try {
                val ability = clsInfo.loadClass()
                    .getDeclaredConstructor()
                    .newInstance() as IAbility

                abils.add(ability)
            } catch (e: Throwable) {
                plugin.logger.warning("Не удалось создать способность ${clsInfo.name}: ${e.message}")
            }
        }

        register(*abils.toTypedArray())

        plugin.logger.info(
            "Зарегистрировано ${abils.size} способностей для плагина ${plugin.name}. " +
                    "Текущее общее количество: ${abilities.size}"
        )
    }

    /**
     * Регистрирует одну или несколько способностей.
     * Проверяет дубликаты, сохраняет конфигурацию и логирует успешную регистрацию.
     *
     * @param abilitiesToAdd список способностей для регистрации
     */
    private fun register(vararg abilitiesToAdd: IAbility) {
        for (ability in abilitiesToAdd) {
            try {
                if (abilities.containsKey(ability.id)) {
                    plugin.logger.warning("Способность с id '${ability.id}' уже зарегистрирована, пропущена.")
                    continue
                }

                if (ability is Listener) {
                    Bukkit.getPluginManager().registerEvents(ability, ERaces.getInstance())
                    plugin.logger.info("Listener для способности '${ability.id}' зарегистрирован.")
                }

                ability.saveDefaultConfig(plugin)
                ability.loadConfig(plugin)

                if (ability is IComboActivatable) {
                    val combo = ability.getComboKey()

                    if (!combo.isNullOrBlank()) {
                        val duplicate = abilities.values
                            .filterIsInstance<IComboActivatable>()
                            .firstOrNull { it.getComboKey() == combo }

                        if (duplicate != null) {
                            val dupId = (duplicate as? IAbility)?.id ?: "unknown"
                            plugin.logger.warning(
                                "Дубликат comboKey '$combo' у способностей '$dupId' и '${ability.id}'. " +
                                        "Комбинация будет работать только для первой зарегистрированной способности."
                            )
                        }
                    }
                }

                abilities[ability.id] = ability
                plugin.logger.info("Зарегистрирована способность: ${ability.id}")
            } catch (e: Exception) {
                AbilityRegistrationException(
                    "Ошибка при регистрации способности '${ability.id}'",
                    e,
                    ability.id
                ).handle()
            }
        }
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
        val context = ERaces.getInstance().context
        val ability = abilities[id]
        val race = context.playerDataManager.getPlayerRace(player)

        if (ability == null) throw PlayerAbilityNotFoundException(player, id)
        if (race == null) throw PlayerRaceNotSelectedException(player)
        if (id !in race.abilities) throw PlayerAbilityNotOwnedException(player, id)

        if (ability is IManaCostAbility) {
            val manaCost = ability.getManaCost()
            if (manaCost > 0) {
                val manaManager = context.manaManager
                val currentMana = manaManager.getMana(player)

                if (currentMana < manaCost) {
                    player.actionMsg("<red>Недостаточно маны! Нужно <yellow>$manaCost<red>, у тебя <yellow>$currentMana")
                    return
                }

                manaManager.useMana(player, manaCost)
            }
        }

        if (ability is ICooldownAbility && AbilityCooldownManager.hasCooldown(player, id)) {
            val remaining = AbilityCooldownManager.getRemaining(player, id)
            throw PlayerAbilityOnCooldownException(player, id, remaining)
        }

        try {
            if (ability is ICooldownAbility) {
                val cooldown = ability.getCooldown()
                if (cooldown > 0) {
                    AbilityCooldownManager.setCooldown(player, id, cooldown)
                }
            }

            ability.activate(player)
        } catch (e: PlayerException) {
            e.handle()
        } catch (t: Throwable) {
            ExceptionProcessor.process(t, player)
            AbilityActivationException(
                "Ошибка при активации способности '$id' у игрока ${player.name}",
                t,
                player
            ).handle()
        }
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
    fun getAbility(id: String): IAbility? = abilities[id]

    /**
     * Возвращает все зарегистрированные способности.
     *
     * @return список всех способностей
     */
    fun getAllAbilities(): List<IAbility> = abilities.values.toList()

    /**
     * Активация способности игроком по комбо-коду.
     *
     * @param player игрок
     * @param combo комбо-код способности
     */
    fun activateByCombo(player: Player, combo: String) {
        val ability = abilities.values
            .filterIsInstance<IComboActivatable>()
            .firstOrNull { it.getComboKey() == combo }

        if (ability == null) {
            player.actionMsg("<red>Нет способности, назначенной на комбинацию <yellow>$combo")
            return
        }

        activate(player, (ability as IAbility).id)
    }
}
