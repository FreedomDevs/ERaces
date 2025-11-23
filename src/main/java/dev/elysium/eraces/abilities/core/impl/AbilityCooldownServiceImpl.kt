package dev.elysium.eraces.abilities.core.impl

import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityCooldownService
import dev.elysium.eraces.abilities.core.utils.AbilityCooldownManager
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.exceptions.player.PlayerAbilityOnCooldownException
import org.bukkit.entity.Player

class AbilityCooldownServiceImpl : IAbilityCooldownService {
    override fun check(player: Player, abilityId: String, ability: ICooldownAbility) {
        if (AbilityCooldownManager.hasCooldown(player, abilityId)) {
            val remaining = AbilityCooldownManager.getRemaining(player, abilityId)
            throw PlayerAbilityOnCooldownException(player, abilityId, remaining)
        }
    }

    override fun apply(player: Player, abilityId: String, ability: ICooldownAbility) {
        ability.getCooldown()
            .takeIf { it > 0 }
            ?.let { seconds ->
                AbilityCooldownManager.setCooldown(player, abilityId, seconds)
            }
    }
}