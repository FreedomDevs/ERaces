package dev.elysium.eraces.abilities.core.registry

import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable

class ComboRegistry {

    private val comboMap: MutableMap<String, String> = mutableMapOf()

    val size: Int
        get() = comboMap.size

    /**
     * Регистрирует comboKey для способности.
     * Если уже существует — возвращает id способности, которая держит эту комбо.
     */
    fun registerCombo(ability: IComboActivatable) {
        ability.getComboKey()
            ?.takeIf { it.isNotBlank() }
            ?.let { combo ->
                (ability as IAbility).id
                    .takeIf { combo !in comboMap }
                    ?.let { comboMap[combo] = it }
            }
    }

    /**
     * Возвращает ID способности по ее comboKey.
     */
    fun getAbilityId(combo: String): String? = comboMap[combo]
}