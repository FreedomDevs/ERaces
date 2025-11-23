package dev.elysium.eraces.abilities.core.interfaces.services.activator

import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import org.bukkit.entity.Player

interface IAbilityManaService {
    fun checkAndConsume(player: Player, ability: IManaCostAbility)
}