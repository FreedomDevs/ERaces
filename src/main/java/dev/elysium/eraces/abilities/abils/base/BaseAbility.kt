package dev.elysium.eraces.abilities.abils.base

import org.bukkit.entity.Player

abstract class BaseAbility(override val id: String) : BaseAbilityWithConfig(id) {
    final override fun activate(player: Player) {
        preActivate(player)
        onActivate(player)
        postActivate(player)
    }

    protected open fun preActivate(player: Player) {}
    protected abstract fun onActivate(player: Player)
    protected open fun postActivate(player: Player) {}

    abstract override fun loadParams(cfg: org.bukkit.configuration.file.YamlConfiguration)
    abstract override fun writeDefaultParams(cfg: org.bukkit.configuration.file.YamlConfiguration)
}
