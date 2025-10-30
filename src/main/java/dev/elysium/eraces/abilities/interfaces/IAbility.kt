package dev.elysium.eraces.abilities.interfaces

import org.bukkit.entity.Player

interface IAbility: IConfigurableAbility {
    val id: String
    fun activate(player: Player)
}