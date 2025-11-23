package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.activator.AbilityActivator
import dev.elysium.eraces.abilities.core.impl.bukkit.BukkitAbilityEnvironmentImpl
import dev.elysium.eraces.abilities.core.impl.bukkit.BukkitAbilityLoggerImpl
import dev.elysium.eraces.abilities.core.impl.bukkit.BukkitAbilityMessengerImpl
import dev.elysium.eraces.abilities.core.impl.bukkit.BukkitManaProviderImpl
import dev.elysium.eraces.abilities.core.impl.bukkit.BukkitPlayerDataProviderImpl
import dev.elysium.eraces.abilities.core.impl.AbilityComboServiceImpl
import dev.elysium.eraces.abilities.core.impl.AbilityCooldownServiceImpl
import dev.elysium.eraces.abilities.core.impl.AbilityManaServiceImpl
import dev.elysium.eraces.abilities.core.impl.AbilityValidationServiceImpl
import dev.elysium.eraces.abilities.core.registry.AbilityRegistrar
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.abilities.core.registry.ComboRegistry

class AbilityContext(plugin: ERaces) {
    private val logger = BukkitAbilityLoggerImpl(plugin)
    private val env = BukkitAbilityEnvironmentImpl(plugin)
    private val messenger = BukkitAbilityMessengerImpl()
    private val playerDataProvider = BukkitPlayerDataProviderImpl()
    private val manaProvider = BukkitManaProviderImpl()

    val registry = AbilityRegistry()
    val comboRegistry = ComboRegistry()

    private val validationService = AbilityValidationServiceImpl(registry, playerDataProvider)
    private val manaService = AbilityManaServiceImpl(manaProvider, messenger)
    private val cooldownService = AbilityCooldownServiceImpl()
    private val comboService = AbilityComboServiceImpl(comboRegistry)

    val registrar = AbilityRegistrar(
        registry = registry,
        combos = comboRegistry,
        logger = logger,
        env = env
    )

    val activator = AbilityActivator(
        registry = registry,
        messenger = messenger,
        validationService = validationService,
        manaService = manaService,
        cooldownService = cooldownService,
        comboService = comboService
    )
}