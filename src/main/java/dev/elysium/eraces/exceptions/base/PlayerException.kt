package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player

/**
 * Базовое исключение для ошибок, связанных с действиями игрока.
 *
 * Используется для ситуаций, когда игрок совершает недопустимое действие,
 * не имея права на это, или возникает проблема с его способностями/расой.
 *
 * @property player игрок, к которому относится ошибка
 * @property code код ошибки из [ErrorCodes], по умолчанию [ErrorCodes.PLAYER_ERROR]
 * @param message сообщение об ошибке, которое будет залогировано и отправлено игроку
 */
abstract class PlayerException(
    val player: Player,
    override val message: String,
    override val code: ErrorCodes = ErrorCodes.PLAYER_ERROR
) : ERacesException(code, message) {

    /**
     * Обработка ошибки игрока.
     *
     * Логирует предупреждение с кодом ошибки, именем игрока и сообщением.
     * Также вызывает [sendToPlayer] для уведомления игрока.
     */
    override fun handle() {
        ERacesLogger.warning("[Player][$code][${player.name}] $message")
        sendToPlayer()
    }

    /**
     * Отправляет сообщение игроку через [ChatUtil].
     *
     * Сообщение окрашено в красный цвет.
     * Метод можно переопределять в дочерних классах для кастомного формата сообщений.
     */
    open fun sendToPlayer() {
        ChatUtil.message(player, "<red>$message")
    }
}