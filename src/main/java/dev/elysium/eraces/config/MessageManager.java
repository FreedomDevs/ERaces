package dev.elysium.eraces.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessageManager {
    private static final String FILE_NAME = "message.yml";

    private final YamlManager cfgManager;
    private YamlConfiguration messagesConfig;

    public MessageManager(JavaPlugin plugin) {
        this.cfgManager = new YamlManager(plugin, FILE_NAME, true);
        loadMessage();
    }

    private void loadMessage() {
        messagesConfig = cfgManager.getConfig();
        messagesConfig.setDefaults(cfgManager.getDefaultConfig());
    }

    public String getString(String path) {
        return messagesConfig.getString(path);
    }

    public String getString(String path, String defaultValue) {
        return messagesConfig.getString(path, defaultValue);
    }

    public List<String> getStringList(String path) {
        return messagesConfig.getStringList(path);
    }
}
