package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.DamageTracker;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.PluginContext;
import dev.elysium.eraces.config.*;
import dev.elysium.eraces.exceptions.InitFailedException;
import dev.elysium.eraces.xpManager.XpManager;
import org.bukkit.Bukkit;

public class ManagerInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        try {
            PluginContext ctx = plugin.getContext();

            // Global config
            GlobalConfigManager globalConfig = new GlobalConfigManager(plugin);
            ctx.setGlobalConfigManager(globalConfig);

            // Races and player data
            ctx.setRacesConfigManager(new RacesConfigManager(plugin));
            ctx.setSpecializationsManager(new SpecializationsManager(plugin, ctx.getDatabase()));
            ctx.setPlayerDataManager(new PlayerDataManager(ctx.getRacesConfigManager().getRaces(), ctx.getDatabase()));

            // XP & Damage tracking
            XpManager xpManager = new XpManager();
            Bukkit.getPluginManager().registerEvents(xpManager, plugin);
            ctx.setXpManager(xpManager);

            DamageTracker damageTracker = new DamageTracker();
            Bukkit.getPluginManager().registerEvents(damageTracker, plugin);
            ctx.setXpDamageTracker(damageTracker);

            // Mana
            ctx.manaManager = new ManaManager(plugin);

        } catch (Exception e) {
            throw new InitFailedException("Ошибка при инициализации менеджеров", e);
        }
    }
}
