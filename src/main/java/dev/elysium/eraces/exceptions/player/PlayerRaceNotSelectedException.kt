package dev.elysium.eraces.exceptions.player

import dev.elysium.eraces.exceptions.PlayerException
import org.bukkit.entity.Player

class PlayerRaceNotSelectedException(player: Player) :
    PlayerException(player, "Ты ещё не выбрал расу!")