package dev.elysium.eraces.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class YamlManager {
    private final String filename;
    private final JavaPlugin plugin;
    private final boolean haveDefault;
    private final File fileData;

    public YamlManager(JavaPlugin plugin, String filename, boolean haveDefault) {
        this.filename = filename;
        this.plugin = plugin;
        this.haveDefault = haveDefault;
        this.fileData = new File(plugin.getDataFolder(), this.filename);
    }

    public YamlConfiguration getConfig() {
        if (fileData.exists()) {
            return YamlConfiguration.loadConfiguration(fileData);
        }

        if (haveDefault) {
            plugin.saveResource(filename, false);
            return YamlConfiguration.loadConfiguration(fileData);
        }

        return new YamlConfiguration();
    }

    public YamlConfiguration getDefaultConfig() {
        InputStream defaultMessage = plugin.getResource(filename);
        if (defaultMessage != null) {
            return YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultMessage, StandardCharsets.UTF_8));
        }
        return new YamlConfiguration();
    }

    public void saveConfig(YamlConfiguration config) {
        try {
            config.save(fileData);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config file: " + filename, e);
        }
    }
}
