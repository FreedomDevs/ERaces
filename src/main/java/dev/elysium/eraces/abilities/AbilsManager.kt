package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.AfterimageAbility
import dev.elysium.eraces.abilities.abils.AmbushAbility
import dev.elysium.eraces.abilities.abils.ArsenalAbility
import dev.elysium.eraces.abilities.abils.BlockAbility
import dev.elysium.eraces.abilities.abils.BloodOfFirstAbility
import dev.elysium.eraces.abilities.abils.BossRushAbility
import dev.elysium.eraces.abilities.abils.BurnAbility
import dev.elysium.eraces.abilities.abils.DeadlyRushAbility
import dev.elysium.eraces.abilities.abils.EroticCharmAbility
import dev.elysium.eraces.abilities.abils.FindHimIfYouCanAbility
import dev.elysium.eraces.abilities.abils.FindMeIfYouCan
import dev.elysium.eraces.abilities.abils.FireBoomAbility
import dev.elysium.eraces.abilities.abils.FireballAbility
import dev.elysium.eraces.utils.ChatUtil
import dev.elysium.eraces.abilities.abils.ForestSpiritAbility
import dev.elysium.eraces.abilities.abils.HopSkipDeepAbility
import dev.elysium.eraces.abilities.abils.MasterTheForestAbility
import dev.elysium.eraces.abilities.abils.RageModeAbility
import dev.elysium.eraces.abilities.abils.SharpClawsAbility
import dev.elysium.eraces.abilities.abils.ShellingAbility
import dev.elysium.eraces.abilities.abils.SupremeMagicianAbility
import dev.elysium.eraces.abilities.abils.TheFlameOfHealingAbility
import dev.elysium.eraces.abilities.abils.TheMagicBarrierAbility
import dev.elysium.eraces.abilities.abils.TheWingedWhirlwindAbility
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class AbilsManager private constructor(private val plugin: ERaces) {
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
            instance ?: throw IllegalStateException("AbilsManager не инициализирован! Вызови init()")

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
     * Регистрирует стандартные способности
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
            TheFlameOfHealingAbility(),
            EroticCharmAbility(),
            FindMeIfYouCan(),
            HopSkipDeepAbility()
        )
        register(*defaultAbilities.toTypedArray())
        plugin.logger.info("Зарегистрировано способностей: ${abilities.size}")
    }

    /**
     * Регистрирует одну или несколько способностей.
     * Проверяет дубликаты, сохраняет конфиг и логирует успешную регистрацию.
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
                plugin.logger.log(Level.SEVERE, "Ошибка при регистрации способности '${ability.id}'", e)
            }
        }
    }

    /**
     * Активация способности игроком.
     */
    fun activate(player: Player, id: String) {
        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(player)
        val ability = abilities[id]

        when {
            race == null -> {
                ChatUtil.legacyMessage(player, "&cТы ещё не выбрал расу!")
                return
            }

            !race.abilities.contains(id) -> {
                ChatUtil.legacyMessage(player, "&cТвоя раса не умеет использовать способность &e$id")
                return
            }

            ability == null -> {
                ChatUtil.legacyMessage(player, "&cСпособность &e$id&c не найдена!")
                return
            }
        }

        if (ability is IManaCostAbility) {
            val manaCost = ability.getManaCost()
            if (manaCost > 0) {
                val manaManager = ERaces.getInstance().context.manaManager
                val currentMana = manaManager.getMana(player)
                if (currentMana < manaCost) {
                    ChatUtil.sendAction(
                        player,
                        "<red>Недостаточно маны! Нужно <yellow>$manaCost<red>, у тебя <yellow>$currentMana"
                    )
                    return
                }
                manaManager.useMana(player, manaCost)
            }
        }

        if (ability is ICooldownAbility && CooldownManager.hasCooldown(player, id)) {
            val remaining = CooldownManager.getRemaining(player, id)
            ChatUtil.legacyMessage(player, "&cСпособность &e$id&c ещё на кулдауне! Осталось &e${remaining}с")
            return
        }

        try {
            ability.activate(player)

            if (ability is ICooldownAbility) {
                val cooldown = ability.getCooldown()
                if (cooldown > 0) {
                    CooldownManager.setCooldown(player, id, cooldown)
                }
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Ошибка при активации способности '$id' у игрока ${player.name}", e)
            ChatUtil.legacyMessage(player, "&cПроизошла ошибка при использовании способности &e$id")
        }
    }

    /**
     * Возвращяет способность по её ID.
     */
    fun getAbility(id: String): IAbility? = abilities[id]

    /**
     * Возвращает все зарегистрированные способности.
     */
    fun getAllAbilities(): List<IAbility> = abilities.values.toList()

    fun activateByCombo(player: Player, combo: String) {
        val ability = abilities.values
            .filterIsInstance<IComboActivatable>()
            .firstOrNull { it.getComboKey() == combo }

        if (ability == null) {
            ChatUtil.sendAction(player, "<red>Нет способности, назначенной на комбинацию <yellow>$combo")
            return
        }

        activate(player, (ability as IAbility).id)
    }


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
