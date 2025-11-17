package dev.elysium.eraces.config;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.datatypes.ReflectionUtils;
import dev.elysium.eraces.exceptions.internal.ConfigLoadException;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RacesConfigManager {
    private static final String FILE_NAME = "races.yml";
    private static final String DIRECTORY_NAME = "races.d";

    @Getter
    private final Map<String, Race> races = new HashMap<>();

    private final YamlManager cfgManager;
    private final JavaPlugin plugin;

    public RacesConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(this.plugin, FILE_NAME, true);
        reloadConfig();
    }


    public void reloadConfig() {
        loadConfig();
        plugin.getLogger().info("Загружено: " + races.size() + " рас");
    }

    private void loadConfig() {
        races.clear();
        Race defaultRace = new Race();
        defaultRace.setId("default");
        races.put("default", defaultRace);
        loadFromConfig(cfgManager.getConfig());

        File dir = new File(plugin.getDataFolder(), DIRECTORY_NAME);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result){
                plugin.getLogger().severe("Не удалось создать папку races.d");
                return;
            }

            String[] defaults = {"elfs.yml", "humans.yml", "orks.yml"}; // все файлы-шаблоны
            for (String f : defaults) {
                try (InputStream in = plugin.getResource(DIRECTORY_NAME + "/" + f)) {
                    if (in != null) {
                        Files.copy(in, new File(dir, f).toPath());
                    }
                } catch (IOException e) {
                    throw new ConfigLoadException("Не удалось скопировать " + f, e);
                }
            }
        }

        if (dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> (name.endsWith(".yml") || name.endsWith(".yaml")));
            if (files != null) {
                for (File file : files) {
                    plugin.getLogger().info("Загрузка конфига: "+file.getName());
                    int racesLoaded = loadFromConfig(YamlConfiguration.loadConfiguration(file));
                    plugin.getLogger().info("Из конфига: "+file.getName()+" загружено "+racesLoaded+" рас");
                }
            }
        }
    }

    private int loadFromConfig(YamlConfiguration config) {
        int racesLoaded = 0;
        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) continue;

            Race race = new Race();
            race.setId(key);

            try {
                ReflectionUtils.loadSection(race, section);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "[%s] race config failed to load".formatted(key), e);
                continue;
            }

            if(races.containsKey(key)){
                plugin.getLogger().severe("Не удалось загрузить расу "+key+", раса уже объявлена в другом конфиге");
                continue;
            }

            races.put(key, race);
            racesLoaded++;
        }
        return racesLoaded;
    }
}
