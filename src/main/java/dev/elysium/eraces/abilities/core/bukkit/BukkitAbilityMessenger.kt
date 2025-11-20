package dev.elysium.eraces.abilities.core.bukkit

import dev.elysium.eraces.abilities.core.interfaces.IAbilityMessenger
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.entity.Player

class BukkitAbilityMessenger : IAbilityMessenger {
    override fun send(player: Player, message: String) {
        player.actionMsg(message)
    }
}