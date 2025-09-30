package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.FireballAbility
import org.bukkit.entity.Player

class AbilsManager(private val plugin: ERaces) {
    private val abilities = mutableMapOf<String, IAbility>()


    private var instance: AbilsManager? = null

    fun init() {
        val allAbilities: List<IAbility> = listOf(
            FireballAbility()
        )

        register(*allAbilities.toTypedArray())
        plugin.logger.info("§aЗарегистрировано способностей: ${abilities.size}")
    }

    fun getInstance(): AbilsManager {
        return instance ?: throw IllegalStateException("AbilsManager не инициализирован! Вызови init()")
    }


    private fun register(vararg abs: IAbility) {
        for (ab in abs) {
            ab.saveDefaultConfig(plugin)
            ab.loadConfig(plugin)
            abilities[ab.id] = ab
        }
    }

    fun activate(player: Player, id: String) {
        val race = ERaces.getPlayerMng().getPlayerRace(player) ?: run {
            player.sendMessage("§cТы ещё не выбрал расу!")
            return
        }

        if (!race.abilsKT.contains(id)) {
            player.sendMessage("§cТвоя раса не умеет использовать способность §e$id")
            return
        }

        val ab = abilities[id]
        if (ab == null) {
            player.sendMessage("§cСпособность §e$id§c не найдена!")
            return
        }

        ab.activate(player)
    }
}

