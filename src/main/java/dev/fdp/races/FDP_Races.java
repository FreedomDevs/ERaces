package dev.fdp.races;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    public Map<String, Race> races = null;
    private static FDP_Races instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    public FDP_Races getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getLogger().info("Загрузка!!!");

        RacesConfigLoader.checkConfigExists(this);

        this.races = RacesConfigLoader.loadConfig(this);
        getLogger().info("Загружено: " + this.races.size() + " расс");
    }

    @Override
    public void onDisable() {
        getLogger().info("Выключение😞");
    }
}
