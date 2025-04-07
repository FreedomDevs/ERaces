package dev.fdp.races;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageManager {
    private final String fileName = "message.yml";
    private final FDP_Races plugin;
    private final File messagesFile;
    private FileConfiguration messagesConfig;

    public MessageManager(FDP_Races plugin){
        this.plugin = plugin;
        this.messagesFile = new File(plugin.getDataFolder(), fileName);
        loadMessage();
    }

    private void loadMessage() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        if (!messagesFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        InputStream defaultMessage = plugin.getResource(fileName);
        if (defaultMessage != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultMessage, StandardCharsets.UTF_8));
            messagesConfig.setDefaults(defaultConfig);
        }
    }

    public void reload() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        InputStream defaultMessage = plugin.getResource(fileName);
        if (defaultMessage != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultMessage, StandardCharsets.UTF_8));
            messagesConfig.setDefaults(defaultConfig);
        }
    }

    public String getString(String path) {
        return messagesConfig.getString(path);
    }

    public String getString(String path, String defaultValue ) {
        return messagesConfig.getString(path, defaultValue);
    }

    public List<String> getStringList(String path) {
        return messagesConfig.getStringList(path);
    }
}

