package dev.elysium.eraces.abilities.interfaces

import org.bukkit.entity.Player

interface IAbility: IConfigurableAbility {
    val id: String
    val name: String?
    val description: String?
    fun activate(player: Player)
}