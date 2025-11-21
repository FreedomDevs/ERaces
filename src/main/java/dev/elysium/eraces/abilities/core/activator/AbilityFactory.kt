package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.abilities.interfaces.IAbility
import kotlin.reflect.KClass

class AbilityFactory {
    fun <T : IAbility> create(klass: KClass<T>): T {
        return klass.java.getDeclaredConstructor().newInstance()
    }
}