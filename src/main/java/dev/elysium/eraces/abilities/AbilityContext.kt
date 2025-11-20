package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces

class AbilityContext(plugin: ERaces) {

    val registry = AbilityRegistry()
    val comboRegistry = ComboRegistry()
    val registrar = AbilityRegistrar(plugin, registry, comboRegistry)
    val activator = AbilityActivator(registry, comboRegistry)
}