package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces

class AbilityContext(plugin: ERaces) {
    private val logger = BukkitAbilityLogger(plugin)
    private val env = BukkitAbilityEnvironment(plugin)

    val registry = AbilityRegistry()
    val comboRegistry = ComboRegistry()
    val registrar = AbilityRegistrar(registry, comboRegistry, logger, env)
    val activator = AbilityActivator(registry, comboRegistry)
}