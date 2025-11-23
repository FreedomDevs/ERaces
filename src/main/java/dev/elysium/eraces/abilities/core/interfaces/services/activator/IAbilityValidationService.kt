package dev.elysium.eraces.abilities.core.interfaces.services.activator

import org.bukkit.entity.Player

interface IAbilityValidationService {
    fun validateAbilityAccess(player: Player, abilityId: String)
}