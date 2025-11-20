package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.exceptions.internal.AbilityRegistrationException
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class AbilityRegistrar(
    private val plugin: ERaces,
    private val abilities: MutableMap<String, IAbility>,
    private val comboRegistry: ComboRegistry
) {

    fun register(vararg toAdd: IAbility) {
        for (ability in toAdd) {

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

        if (abilities.containsKey(id)) {
            plugin.logger.warning("Способность с id '$id' уже зарегистрирована, пропущена.")
            return
        }

        registerListenerIfNeeded(ability)
        loadConfigs(ability)
        warnIfDuplicateCombo(ability)

        abilities[id] = ability
        plugin.logger.info("Зарегистрирована способность: $id")
    }

    private fun registerListenerIfNeeded(ability: IAbility) {
        if (ability is Listener) {
            Bukkit.getPluginManager().registerEvents(ability, plugin)
            plugin.logger.info("Listener для способности '${ability.id}' зарегистрирован.")
        }
    }

    private fun loadConfigs(ability: IAbility) {
        ability.saveDefaultConfig(plugin)
        ability.loadConfig(plugin)
    }

    private fun warnIfDuplicateCombo(ability: IAbility) {
        if (ability !is IComboActivatable) return

        val combo = ability.getComboKey()
        if (combo.isNullOrBlank()) return

        val existing = comboRegistry.getAbilityId(combo)

        if (existing != null) {
            plugin.logger.warning(
                "Дубликат comboKey '$combo': уже назначен для '$existing'. " +
                        "Способность '${ability.id}' пропущена."
            )
        } else {
            comboRegistry.registerCombo(ability)
        }
    }
}