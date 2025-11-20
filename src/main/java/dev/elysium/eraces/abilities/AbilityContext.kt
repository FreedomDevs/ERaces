package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.activator.AbilityActivator
import dev.elysium.eraces.abilities.core.bukkit.BukkitAbilityEnvironment
import dev.elysium.eraces.abilities.core.bukkit.BukkitAbilityLogger
import dev.elysium.eraces.abilities.core.bukkit.BukkitAbilityMessenger
import dev.elysium.eraces.abilities.core.bukkit.BukkitManaProvider
import dev.elysium.eraces.abilities.core.bukkit.BukkitPlayerDataProvider
import dev.elysium.eraces.abilities.core.registry.AbilityRegistrar
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.abilities.core.registry.ComboRegistry

class AbilityContext(plugin: ERaces) {
    private val logger = BukkitAbilityLogger(plugin)
    private val env = BukkitAbilityEnvironment(plugin)
    private val messenger = BukkitAbilityMessenger()
    private val playerDataProvider = BukkitPlayerDataProvider()
    private val manaProvider = BukkitManaProvider()

    val registry = AbilityRegistry()
    val comboRegistry = ComboRegistry()
    val registrar = AbilityRegistrar(registry, comboRegistry, logger, env)
    val activator = AbilityActivator(registry, comboRegistry, messenger, playerDataProvider, manaProvider)
}