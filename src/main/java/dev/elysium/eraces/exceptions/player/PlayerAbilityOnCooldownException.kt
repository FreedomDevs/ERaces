package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.PlayerException
import org.bukkit.entity.Player

class PlayerAbilityOnCooldownException(player: Player, abilityId: String, remainingSeconds: Long) :
    PlayerException(player, "Способность $abilityId ещё на кулдауне! Осталось ${remainingSeconds}s")