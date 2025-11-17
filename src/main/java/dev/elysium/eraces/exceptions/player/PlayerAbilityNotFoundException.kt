package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.PlayerException
import org.bukkit.entity.Player

/**
 * Ошибка: игрок пытается использовать способность, которой не существует.
 * @param player Игрок
 * @param abilityId ID способности
 * @param message Сообщение об ошибке. Если не передано, используется дефолтное.
 */
class PlayerAbilityNotFoundException(
    player: Player,
    abilityId: String,
    message: String? = null
) : PlayerException(
    player,
    message ?: "Способность $abilityId не найдена!",
    ErrorCodes.PLAYER_ABILITY_NOT_FOUND
)