package dev.elysium.eraces.abilities.core.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.IAbilityEnvironment
import dev.elysium.eraces.abilities.interfaces.IAbility
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class BukkitAbilityEnvironment(private val plugin: ERaces) : IAbilityEnvironment {
    override fun registerListener(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
    }

    override fun saveDefaultConfig(ability: IAbility) {
        ability.saveDefaultConfig(plugin)
    }

    override fun loadConfig(ability: IAbility) {
        ability.loadConfig(plugin)
    }
}