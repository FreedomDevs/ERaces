package dev.elysium.eraces.abilities.core.impl

import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityComboService
import dev.elysium.eraces.abilities.core.registry.ComboRegistry

class AbilityComboServiceImpl(
    private val combos: ComboRegistry
) : IAbilityComboService {

    override fun resolve(combo: String): String? =
        combos.getAbilityId(combo)
}