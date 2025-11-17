package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player

/**
 * Базовое исключение для ошибок игрока.
 * @param player Игрок, к которому относится ошибка
 * @param message Сообщение об ошибке
 * @param code Код ошибки
 */
abstract class PlayerException(
    val player: Player,
    override val message: String,
    override val code: String = "PLAYER_ERROR"
) : ERacesException(code, message) {

    override fun handle() {
        ERacesLogger.warning("[Player][$code][${player.name}] $message")
        sendToPlayer()
    }

    /**
     * Отправляет сообщение игроку.
     */
    open fun sendToPlayer() {
        ChatUtil.message(player, "<red>$message")
    }
}
