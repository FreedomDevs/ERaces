package dev.elysium.eraces.abilities.core.interfaces

import dev.elysium.eraces.abilities.interfaces.IAbility
import org.bukkit.event.Listener

interface IAbilityEnvironment {
    fun registerListener(listener: Listener)
    fun saveDefaultConfig(ability: IAbility)
    fun loadConfig(ability: IAbility)
}