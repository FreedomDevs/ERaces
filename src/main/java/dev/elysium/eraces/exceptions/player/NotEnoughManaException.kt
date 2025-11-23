package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.base.PlayerException
import org.bukkit.entity.Player

class NotEnoughManaException(
    player: Player,
    message: String
) : PlayerException(player, message)