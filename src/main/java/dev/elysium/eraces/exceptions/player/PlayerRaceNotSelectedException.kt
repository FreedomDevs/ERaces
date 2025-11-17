package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.PlayerException
import org.bukkit.entity.Player

/**
 * Ошибка: игрок ещё не выбрал расу.
 *
 * @param player Игрок
 * @param message Сообщение об ошибке. Если не передано, используется дефолтное.
 */
class PlayerRaceNotSelectedException(
    player: Player,
    message: String? = null
) : PlayerException(
    player,
    message ?: "Ты ещё не выбрал расу!",
    ErrorCodes.PLAYER_RACE_NOT_SELECTED
)