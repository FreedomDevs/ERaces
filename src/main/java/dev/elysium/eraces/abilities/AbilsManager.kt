package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.BlockAbility
import dev.elysium.eraces.abilities.abils.BossRushAbility
import dev.elysium.eraces.abilities.abils.FireballAbility
import dev.elysium.eraces.abilities.abils.RageModeAbility
import org.bukkit.entity.Player
import java.util.logging.Level

class AbilsManager(private val plugin: ERaces) {
    private val abilities = mutableMapOf<String, IAbility>()

    companion object {
        @Volatile
        private var instance: AbilsManager? = null

        @JvmStatic
        fun getInstance(): AbilsManager {
            return instance ?: throw IllegalStateException()
        }
    }

    @JvmOverloads
    fun init() {
        if (instance != null) {
            plugin.logger.warning("AbilsManager уже инициализирован!")
            return
        }

        instance = this

        val allAbilities: List<IAbility> = listOf(
            FireballAbility(),
            BlockAbility(),
            RageModeAbility(),
            BossRushAbility()
        )

        register(*allAbilities.toTypedArray())
        plugin.logger.info("§aЗарегистрировано способностей: ${abilities.size}")
    }

    fun getInstance(): AbilsManager {
        return instance ?: throw IllegalStateException("AbilsManager не инициализирован! Вызови init()")
    }


    private fun register(vararg abs: IAbility) {
        for (ab in abs) {
            try {
                ab.saveDefaultConfig(plugin)
                ab.loadConfig(plugin)
                if (abilities.containsKey(ab.id)) {
                    plugin.logger.warning("Способность с id ${ab.id} уже зарегистрирована, пропущена.")
                    continue
                }
                abilities[ab.id] = ab
            } catch (e: Exception) {
                plugin.logger.log(Level.SEVERE, "Ошибка при регистрации способности ${ab.id}", e)
            }

        }
    }

    fun activate(player: Player, id: String) {
        val race = ERaces.getPlayerMng().getPlayerRace(player)
        if (race == null) {
            player.sendMessage("§cТы ещё не выбрал расу!")
            return
        }

        if (!race.abilsKT.contains(id)) {
            player.sendMessage("§cТвоя раса не умеет использовать способность §e$id")
            return
        }

        val ability = abilities[id]
        if (ability == null) {
            player.sendMessage("§cСпособность §e$id§c не найдена!")
            return
        }

        try {
            ability.activate(player)
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Ошибка при активации способности ${id} игроком ${player.name}", e)
            player.sendMessage("§cПроизошла ошибка при использовании способности §e$id")
        }
    }

//    Навсякий мб пригодится
    @JvmName("getAbilityJava")
    fun getAbility(id: String): IAbility? = abilities[id]

    @JvmName("getAllAbilitiesJava")
    fun getAllAbilities(): List<IAbility> = ArrayList(abilities.values)
}