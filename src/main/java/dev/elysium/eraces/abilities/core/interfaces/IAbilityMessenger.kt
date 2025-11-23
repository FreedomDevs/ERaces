package dev.elysium.eraces.abilities.core.interfaces

import org.bukkit.entity.Player

interface IAbilityMessenger {
    fun send(player: Player, message: String)
}