package dev.elysium.eraces.abilities.core.impl

import dev.elysium.eraces.abilities.core.interfaces.providers.IPlayerDataProvider
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityValidationService
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotFoundException
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotOwnedException
import dev.elysium.eraces.exceptions.player.PlayerRaceNotSelectedException
import org.bukkit.entity.Player

class AbilityValidationServiceImpl(
    private val registry: AbilityRegistry,
    private val playerDataProvider: IPlayerDataProvider
) : IAbilityValidationService {
    override fun validateAbilityAccess(player: Player, abilityId: String) {
        registry.get(abilityId)
            ?: throw PlayerAbilityNotFoundException(player, abilityId)

        val race = playerDataProvider.getPlayerRace(player)
            ?: throw PlayerRaceNotSelectedException(player)

        if (abilityId !in race.abilities)
            throw PlayerAbilityNotOwnedException(player, abilityId)
    }
}
