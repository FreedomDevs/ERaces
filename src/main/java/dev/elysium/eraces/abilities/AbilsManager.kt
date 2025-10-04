package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.FireballAbility
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player
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
                plugin.logger.warning("⚠️ AbilsManager уже инициализирован, повторная инициализация пропущена.")
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
            FireballAbility()
        )
        register(*defaultAbilities.toTypedArray())
        plugin.logger.info("&aЗарегистрировано способностей: ${abilities.size}")
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

                ability.saveDefaultConfig(plugin)
                ability.loadConfig(plugin)

                abilities[ability.id] = ability
                plugin.logger.info("&aЗарегистрирована способность: ${ability.id}")
            } catch (e: Exception) {
                plugin.logger.log(Level.SEVERE, "Ошибка при регистрации способности '${ability.id}'", e)
            }
        }
    }

    /**
     * Активация способности игроком.
     */
    fun activate(player: Player, id: String) {
        val race = ERaces.getPlayerMng().getPlayerRace(player)
        val ability = abilities[id]

        when {
            race == null -> {
                ChatUtil.legacyMessage(player, "&cТы ещё не выбрал расу!")
                return
            }

            !race.abilsKT.contains(id) -> {
                ChatUtil.legacyMessage(player, "&cТвоя раса не умеет использовать способность &e$id")
                return
            }

            ability == null -> {
                ChatUtil.legacyMessage(player, "&cСпособность &e$id&c не найдена!")
                return
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
     * Получает способность по её ID.
     */
    fun getAbility(id: String): IAbility? = abilities[id]

    /**
     * Возвращает все зарегистрированные способности.
     */
    fun getAllAbilities(): List<IAbility> = abilities.values.toList()

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
