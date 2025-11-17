package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.PlayerException
import org.bukkit.entity.Player

class PlayerAbilityNotFoundException(player: Player, abilityId: String) :
    PlayerException(player, "Способность $abilityId не найдена!")