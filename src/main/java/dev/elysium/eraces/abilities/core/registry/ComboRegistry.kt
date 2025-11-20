package dev.elysium.eraces.abilities.core.registry

import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable

class ComboRegistry {

    private val comboMap: MutableMap<String, String> = mutableMapOf()

    /**
     * Регистрирует comboKey для способности.
     * Если уже существует — возвращает id способности, которая держит эту комбо.
     */
    fun registerCombo(ability: IComboActivatable) {
        val combo = ability.getComboKey() ?: return
        if (combo.isBlank()) return

        // Не заменяем уже существующую комбо
        if (!comboMap.containsKey(combo)) {
            comboMap[combo] = (ability as IAbility).id
        }
    }

    /**
     * Возвращает ID способности по ее comboKey.
     */
    fun getAbilityId(combo: String): String? {
        return comboMap[combo]
    }
}