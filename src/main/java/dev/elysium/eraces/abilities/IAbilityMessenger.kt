package dev.elysium.eraces.abilities

import org.bukkit.entity.Player

interface IAbilityMessenger {
    fun send(player: Player, message: String)
}