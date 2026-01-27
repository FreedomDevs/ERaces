package dev.elysium.eraces.abilities.core.registry

import dev.elysium.eraces.abilities.core.interfaces.IAbilityEnvironment
import dev.elysium.eraces.abilities.core.interfaces.IAbilityLogger
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.exceptions.internal.AbilityRegistrationException
import org.bukkit.event.Listener

class AbilityRegistrar(
    private val registry: AbilityRegistry,
    private val combos: ComboRegistry,
    private val logger: IAbilityLogger,
    private val env: IAbilityEnvironment
) {

    fun register(vararg toAdd: IAbility) {
        toAdd.forEach { ability ->
            try {
                registerSingle(ability)
            } catch (e: Exception) {
                AbilityRegistrationException(
                    "Ошибка при регистрации способности '${ability.id}'",
                    e,
                    ability.id
                ).handle()
            }
        }
    }

    private fun registerSingle(ability: IAbility) {
        val id = ability.id

        if (registry.contains(id)) {
            logger.warn("Способность с id '$id' уже зарегистрирована, пропущена.")
            return
        }

        registerListenerIfNeeded(ability)
        loadConfigs(ability)
        warnIfDuplicateCombo(ability)

        registry.register(ability)
        logger.info("Зарегистрирована способность: $id")
    }

    private fun registerListenerIfNeeded(ability: IAbility) {
        if (ability is Listener) {
            env.registerListener(ability)
            logger.info("Listener для способности '${ability.id}' зарегистрирован.")
        }
    }

    private fun loadConfigs(ability: IAbility) {
        env.saveDefaultConfig(ability)
        env.loadConfig(ability)
    }

    private fun warnIfDuplicateCombo(ability: IAbility) {
        if (ability !is IComboActivatable) return

        val combo = ability.getComboKeyy()
        if (combo.isNullOrBlank()) return

        val existing = combos.getAbilityId(combo)

        if (existing != null) {
            logger.warn("Дубликат comboKey '$combo': уже назначен для '$existing'. " +
                    "Способность '${ability.id}' пропущена.")
        } else {
            combos.registerCombo(ability)
        }
    }
}