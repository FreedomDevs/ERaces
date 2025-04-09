package dev.fdp.races;

import java.util.Map;

import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.events.*;
import dev.fdp.races.updaters.*;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    public Map<String, Race> races = null;
    public RaceManager raceManager;
    public MessageManager messageManager;
    private static FDP_Races instance;
    private BiomeSpeedUpdater biomeSpeedUpdator;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static FDP_Races getInstance() {
        return instance;
    }

    public void reloadConfig() {
        this.races = RacesConfigLoader.loadConfig(this);
        getLogger().info("Загружено: " + this.races.size() + " расс");
    }

    @Override
    public void onEnable() {
        messageManager = new MessageManager(this);

        getLogger().info(messageManager.getString("plugin.enabled"));

        RacesConfigLoader.checkConfigExists(this);

        reloadConfig();

        biomeSpeedUpdator = new BiomeSpeedUpdater();

        raceManager = new RaceManager(getDataFolder(), this);

        Listener[] listeners = {
                new PlayerJoinListener(),
                new PlayerLeaveListener(),
                new PlayerRespawnListener(),
                new ShieldUsageUpdater(),
                new ForbiddenFoodsUpdater(),
                new HandDamageUpdater(),
                new AxeDamageUpdater(),
                new SwordDamageUpdater(),
                new BowDamageUpdater()
        };

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        biomeSpeedUpdator.startTask(this);

        getCommand("races").setExecutor(new RacesCommand());
        getCommand("races").setTabCompleter(new RacesCommand());

        RacesReloader.reloadRaceForAllPlayers();
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getString("plugin.disabled", "Выключение😞"));
    }

}
