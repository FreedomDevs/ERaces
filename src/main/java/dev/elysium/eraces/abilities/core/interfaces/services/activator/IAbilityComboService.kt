package dev.elysium.eraces.abilities.core.interfaces.services.activator

interface IAbilityComboService {
    fun resolve(combo: String): String?
}