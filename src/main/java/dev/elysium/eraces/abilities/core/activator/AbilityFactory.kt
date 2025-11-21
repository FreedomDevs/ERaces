package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.abilities.interfaces.IAbility
import kotlin.reflect.KClass

class AbilityFactory {
    fun <T : IAbility> create(klass: KClass<T>): T {
        return try {
            klass.java.getDeclaredConstructor().newInstance()
        } catch (e: Exception) {
            throw IllegalArgumentException("Не удалось создать экземпляр способности: ${klass.simpleName}", e)
        }
    }
}