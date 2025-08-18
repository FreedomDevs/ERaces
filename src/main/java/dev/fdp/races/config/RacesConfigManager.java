package dev.fdp.races.config;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.datatypes.ReflectionUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RacesConfigManager {
    private static final String FILE_NAME = "races.yml";

    @Getter
    private Map<String, Race> races = null;

    private final YamlManager cfgManager;
    private final JavaPlugin plugin;

    public RacesConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(this.plugin, FILE_NAME, true);
        reloadConfig();
    }


    public void reloadConfig() {
        races = loadConfig();
        plugin.getLogger().info("Загружено: " + races.size() + " рас");
    }

    private Map<String, Race> loadConfig() {
        Map<String, Race> races = new HashMap<>();
        YamlConfiguration config = cfgManager.getConfig();

        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
                continue;

            Race race = new Race();
            race.setId(key);

            try {
                ReflectionUtils.loadSection(race, section);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "[%s] race config failed to load".formatted(key), e);
                continue;
            }

            races.put(key, race);
        }
        return races;
    }
}
