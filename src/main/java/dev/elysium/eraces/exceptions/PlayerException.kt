package dev.elysium.eraces.exceptions

import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player

abstract class PlayerException(
    val player: Player,
    override val message: String,
    override val code: String = "PLAYER_ERROR"
) : ERacesException(code, message) {

    override fun handle() {
        println("[ERaces][Player][$code][${player.name}] $message")
        ChatUtil.message(player, "§c$message")
    }
}