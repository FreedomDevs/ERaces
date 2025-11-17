package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.PlayerException
import org.bukkit.entity.Player

class PlayerAbilityNotOwnedException(player: Player, abilityId: String) :
    PlayerException(player, "Твоя раса не умеет использовать способность $abilityId")