package dev.elysium.eraces.abilities

import dev.elysium.eraces.abilities.interfaces.IAbility

class AbilityRegistry {
    private val abilities: MutableMap<String, IAbility> = mutableMapOf()

    fun register(vararg ability: IAbility) {
        for (a in ability) abilities[a.id] = a
    }

    fun contains(id: String): Boolean = abilities.containsKey(id)

    fun get(id: String): IAbility? = abilities[id]

    fun getAll(): Collection<IAbility> = abilities.values

    fun size(): Int = abilities.size
}