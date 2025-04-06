package dev.fdp.races;

import java.util.Map;

import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.events.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    public Map<String, Race> races = null;
    public RaceManager raceManager;
    private static FDP_Races instance;

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
        getLogger().info("Загрузка!!!");

        RacesConfigLoader.checkConfigExists(this);

        reloadConfig();

        raceManager = new RaceManager(getDataFolder(), this);

        Listener[] listeners = {
                new PlayerJoinListener(raceManager),
                new HeathUpdater(raceManager),
                new MineSpeedUpdater(raceManager),
                new AttackRangeUpdater(raceManager),
                new AdditionalArmorUpdater(raceManager)
        };

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        RacesReloader.reloadRaceForAllPlayers();

        getCommand("races").setExecutor(new RacesCommand());
        getCommand("races").setTabCompleter(new RacesCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Выключение😞");
    }
}
