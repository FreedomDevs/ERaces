package dev.elysium.eraces.abilities.core.interfaces.services.activator

import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import org.bukkit.entity.Player

interface IAbilityCooldownService {
    fun check(player: Player, abilityId: String, ability: ICooldownAbility)
    fun apply(player: Player, abilityId: String, ability: ICooldownAbility)
}