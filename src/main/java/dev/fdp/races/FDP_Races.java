package dev.fdp.races;

import java.util.Map;

import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    public Map<String, Race> races = null;
    public RaceManager raceManager;
    public MessageManager messageManager;
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
        messageManager = new MessageManager(this);

        getLogger().info(messageManager.getString("plugin.enabled"));

        RacesConfigLoader.checkConfigExists(this);

        reloadConfig();

        raceManager = new RaceManager(getDataFolder(), this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(raceManager), this);

        getCommand("races").setExecutor(new RacesCommand());
        getCommand("races").setTabCompleter(new RacesCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getString("plugin.disabled", "Выключение😞"));
    }

}
