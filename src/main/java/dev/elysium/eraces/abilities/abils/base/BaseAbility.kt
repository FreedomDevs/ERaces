package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.exceptions.internal.AbilityActivationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.logging.Level

abstract class BaseAbility(override val id: String) : BaseAbilityWithConfig(id) {
    final override fun activate(player: Player) {
        try {
            preActivate(player)
            onActivate(player)
            postActivate(player)
        } catch (e: Exception) {
            AbilityActivationException(
                message = "Ошибка активации способности '$id' у игрока ${player.name}",
                cause = e,
                player = player
            ).handle()
        }
    }

    protected open fun preActivate(player: Player) {}
    protected abstract fun onActivate(player: Player)
    protected open fun postActivate(player: Player) {}

    abstract override fun loadParams(cfg: YamlConfiguration)
    abstract override fun writeDefaultParams(cfg: YamlConfiguration)
}
