package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.configs.ConfigsProperty;
import dev.elysium.eraces.datatypes.configs.MessageConfigData;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

public class MessageManager {

    private static final String FOLDER_NAME = "messages";

    private final YamlManager cfgManager;
    private YamlConfiguration config;
    private YamlConfiguration defaultConfig;

    @Getter
    private MessageConfigData data;

    public MessageManager(ERaces plugin, String selectedLang) {
        File folder = new File(plugin.getDataFolder(), FOLDER_NAME);
        if (!folder.exists() && !folder.mkdirs()) {
            plugin.getLogger().warning("Failed to create messages folder: " + folder.getAbsolutePath());
        }

        File file = new File(folder, selectedLang.toLowerCase() + ".yml");
        this.cfgManager = new YamlManager(plugin, FOLDER_NAME + "/" + file.getName(), false);

        loadMessage();
    }

    private void loadMessage() {
        this.config = cfgManager.getConfig();
        this.defaultConfig = cfgManager.getDefaultConfig();
        this.data = new MessageConfigData();

        for (Field field : MessageConfigData.class.getDeclaredFields()) {
            field.setAccessible(true);

            ConfigsProperty annotation = field.getAnnotation(ConfigsProperty.class);
            if (annotation == null) continue;

            String path = annotation.path();
            Object value = getOrCreateValue(path, annotation);

            try {
                if (field.getType() == String.class) {
                    field.set(data, value.toString());
                } else if (field.getType() == List.class) {
                    field.set(data, config.getStringList(path));
                }
            } catch (IllegalAccessException e) {
                ERaces.getInstance().getLogger().log(Level.SEVERE,
                        "Failed to set field '" + field.getName() + "' in MessageConfigData", e);

            }
        }
    }

    private Object getOrCreateValue(String path, ConfigsProperty annotation) {
        if (!config.contains(path)) {
            config.set(path, annotation.defaultString());
            cfgManager.saveConfig(config);
            return annotation.defaultString();
        }

        if (config.contains(path)) return config.get(path);
        if (defaultConfig.contains(path)) return defaultConfig.get(path);

        return annotation.defaultString();
    }

    public void reload() {
        loadMessage();
    }
}