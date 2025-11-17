package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.attack.aoe.*
import dev.elysium.eraces.abilities.abils.attack.magic_damage.*
import dev.elysium.eraces.abilities.abils.buffs.mana.*
import dev.elysium.eraces.abilities.abils.buffs.self_buffs.*
import dev.elysium.eraces.abilities.abils.control.aoe_debuff.*
import dev.elysium.eraces.abilities.abils.control.stasis.*
import dev.elysium.eraces.abilities.abils.movement.dash.*
import dev.elysium.eraces.abilities.abils.movement.jumps.*
import dev.elysium.eraces.abilities.abils.special.falling_objects.*
import dev.elysium.eraces.abilities.abils.special.focus_target.*
import dev.elysium.eraces.abilities.abils.support.ally_buffs.*
import dev.elysium.eraces.abilities.interfaces.*
import dev.elysium.eraces.exceptions.ExceptionProcessor
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.exceptions.internal.*
import dev.elysium.eraces.exceptions.player.*
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


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

            instance = AbilsManager(plugin).apply { registerDefaultAbilities() }
            plugin.logger.info("AbilsManager успешно инициализирован.")
        }
    }

    /**
     * Регистрирует стандартные способности плагина.
     * Автоматически вызывается при инициализации.
     */
    private fun registerDefaultAbilities() {
        val defaultAbilities = listOf<IAbility>(
            FireballAbility(),
            BlockAbility(),
            BossRushAbility(),
            BurnAbility(),
            ForestSpiritAbility(),
            RageModeAbility(),
            FireBoomAbility(),
            TheMagicBarrierAbility(),
            AfterimageAbility(),
            DeadlyRushAbility(),
            MasterTheForestAbility(),
            AmbushAbility(),
            BloodOfFirstAbility(),
            SupremeMagicianAbility(),
            ShellingAbility(),
            ArsenalAbility(),
            SharpClawsAbility(),
            TheWingedWhirlwindAbility(),
            TheFlameOfHealingAbility(),
            FindHimIfYouCanAbility(),
            EroticCharmAbility(),
            FindMeIfYouCan(),
            HopSkipDeepAbility(),
            OldAcquaintancesAbility(),
            SkillMastersAbility(),
            JerkAbility(),
            DiveAbility(),
            CrossProtectionAbility(),
            TerrifyingRageAbility(),
            ShadowStepAbility(),
            HolyBodyAbility(),
            TurretAbility(),
            AncientKnowledgeAbility(),
            TheJerkAbility(),
            ShadowJerkAbility(),
            TheArboristAbility(),
            StaticRadiusAbility(),
            ShamanRadiusAbility()
        )
        register(*defaultAbilities.toTypedArray())
        plugin.logger.info("Зарегистрировано способностей: ${abilities.size}")
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
                val ex = AbilityRegistrationException(
                    "Ошибка при регистрации способности '${ability.id}'",
                    e,
                    ability.id
                )
                ex.handle()
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
        val ability = abilities[id]
        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(player)

        when {
            ability == null -> throw PlayerAbilityNotFoundException(player, id)
            race == null -> throw PlayerRaceNotSelectedException(player)
            !race.abilities.contains(id) -> throw PlayerAbilityNotOwnedException(player, id)
            else -> { /* ok */
            }
        }

        if (ability is IManaCostAbility) {
            val manaCost = ability.getManaCost()
            if (manaCost > 0) {
                val manaManager = ERaces.getInstance().context.manaManager
                val currentMana = manaManager.getMana(player)
                if (currentMana < manaCost) {
                    player.actionMsg("<red>Недостаточно маны! Нужно <yellow>$manaCost<red>, у тебя <yellow>$currentMana")
                    return
                }
                manaManager.useMana(player, manaCost)
            }
        }

        if (ability is ICooldownAbility && CooldownManager.hasCooldown(player, id)) {
            val remaining = CooldownManager.getRemaining(player, id)
            throw PlayerAbilityOnCooldownException(player, id, remaining)
        }

        try {
            if (ability is ICooldownAbility) {
                val cooldown = ability.getCooldown()
                if (cooldown > 0) {
                    CooldownManager.setCooldown(player, id, cooldown)
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
        CooldownManager.resetCooldown(player, abilityId)
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

    /**
     * Внутренний менеджер кулдаунов способностей.
     * Хранит время окончания кулдауна для каждого игрока и способности.
     */
    private object CooldownManager {
        private val cooldowns: MutableMap<UUID, MutableMap<String, Long>> = ConcurrentHashMap()

        fun hasCooldown(player: Player, abilityId: String): Boolean {
            val expireTime = cooldowns[player.uniqueId]?.get(abilityId) ?: return false
            return System.currentTimeMillis() < expireTime
        }

        fun getRemaining(player: Player, abilityId: String): Long {
            val expireTime = cooldowns[player.uniqueId]?.get(abilityId) ?: return 0
            val remaining = expireTime - System.currentTimeMillis()
            return if (remaining > 0) TimeUnit.MILLISECONDS.toSeconds(remaining) else 0
        }

        fun setCooldown(player: Player, abilityId: String, seconds: Long) {
            val playerCooldowns = cooldowns.computeIfAbsent(player.uniqueId) { ConcurrentHashMap() }
            playerCooldowns[abilityId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds)
        }

        fun resetCooldown(player: Player, abilityId: String) {
            cooldowns[player.uniqueId]?.remove(abilityId)
        }
    }
}
