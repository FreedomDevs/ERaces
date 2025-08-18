package dev.fdp.races.config;

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
    private final File filedata;

    public YamlManager(JavaPlugin plugin, String filename, boolean haveDefault) {
        this.filename = filename;
        this.plugin = plugin;
        this.haveDefault = haveDefault;
        this.filedata = new File(plugin.getDataFolder(), this.filename);
    }

    public YamlConfiguration getConfig() {
        if (filedata.exists()) {
            return YamlConfiguration.loadConfiguration(filedata);
        }

        if (haveDefault) {
            plugin.saveResource(filename, false);
            return YamlConfiguration.loadConfiguration(filedata);
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
            config.save(filedata);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config file: " + filename, e);
        }
    }
}
