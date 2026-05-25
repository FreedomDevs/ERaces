package dev.elysium.eraces.abilities.interfaces

import dev.elysium.eraces.utils.TimeValue

interface ICooldownAbility {
    fun getCooldown(): TimeValue
}