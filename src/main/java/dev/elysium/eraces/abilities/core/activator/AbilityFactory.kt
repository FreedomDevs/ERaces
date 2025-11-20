package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.abilities.interfaces.IAbility
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AbilityFactory {
    fun <T: IAbility> create(cls: KClass<T>): IAbility {
        return try {
            cls.createInstance()
        } catch (e: Throwable) {
            throw RuntimeException("Не удалось создать способность: ${cls.simpleName}", e)
        }
    }
}