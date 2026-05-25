package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.utils.TimeValue
import org.bukkit.entity.Player

/**
 * Ошибка: игрок пытается использовать способность, которая ещё на кулдауне.
 *
 * @param player Игрок
 * @param abilityId ID способности
 * @param remainingMillis Время, оставшееся до конца кулдауна
 * @param message Сообщение об ошибке. Если не передано, используется дефолтное.
 */
class PlayerAbilityOnCooldownException(
    player: Player,
    abilityId: String,
    val remainingMillis: Long,
    message: String? = null
) : PlayerException(
    player,
    message ?: "Способность $abilityId ещё на кулдауне! Осталось ${TimeValue(remainingMillis).prettify()}",
    ErrorCodes.PLAYER_ABILITY_COOLDOWN
)
