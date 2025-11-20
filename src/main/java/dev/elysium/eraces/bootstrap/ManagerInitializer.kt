package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.DamageTracker
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.abilities.AbilsManager
import dev.elysium.eraces.config.*
import dev.elysium.eraces.exceptions.internal.InitFailedException
import dev.elysium.eraces.gui.raceSelect.RaceSelectMenuPages
import dev.elysium.eraces.xpManager.XpManager
import org.bukkit.Bukkit

class ManagerInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        try {
            val ctx = plugin.context

            RaceSelectMenuPages.registerDefaults()
            RacesReloader.startListeners(plugin)

            // Global config
            val globalConfig = GlobalConfigManager(plugin)
            ctx.globalConfigManager = globalConfig

            // Races and player data
            ctx.racesConfigManager = RacesConfigManager(plugin)
            ctx.specializationsManager = SpecializationsManager(plugin, ctx.database)
            ctx.playerDataManager = PlayerDataManager(ctx.racesConfigManager.getRaces(), ctx.database)

            // XP & Damage tracking
            val xpManager = XpManager()
            Bukkit.getPluginManager().registerEvents(xpManager, plugin)
            ctx.xpManager = xpManager

            val damageTracker = DamageTracker()
            Bukkit.getPluginManager().registerEvents(damageTracker, plugin)
            ctx.xpDamageTracker = damageTracker

            // Mana
            ctx.manaManager = ManaManager(plugin)

            // Abils Manager
            AbilsManager.init(plugin)
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при инициализации менеджеров", e)
        }
    }
}
