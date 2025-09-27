package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.FieldType;
import dev.elysium.eraces.datatypes.configs.GlobalConfigData;
import dev.elysium.eraces.datatypes.configs.ConfigsProperty;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Field;

public class GlobalConfigManager {

    private final YamlManager cfgManager;
    private YamlConfiguration config;

    @Getter
    private GlobalConfigData data;

    private static final String FILE_NAME = "config.yml";

    public GlobalConfigManager(ERaces plugin) throws IllegalAccessException {
        this.cfgManager = new YamlManager(plugin, FILE_NAME, true);
        loadConfig();
    }

    public void loadConfig() throws IllegalAccessException {
        this.config = cfgManager.getConfig();
        this.data = new GlobalConfigData();

        for (Field field : GlobalConfigData.class.getDeclaredFields()) {
            field.setAccessible(true);

            ConfigsProperty annotation = field.getAnnotation(ConfigsProperty.class);
            if (annotation == null) continue;

            String path = annotation.path();
            FieldType type = annotation.type();

            Object value = getOrCreateConfigValue(path, type, annotation, field.getName());
            field.set(data, value);
        }
    }

    private Object getOrCreateConfigValue(String path, FieldType type, ConfigsProperty annotation, String fieldName) {
        if (!config.contains(path)) {
            Object defaultValue = getDefaultFromAnnotation(type, annotation, fieldName);
            config.set(path, defaultValue);
            cfgManager.saveConfig(config);
            return defaultValue;
        }
        return getValueByType(type, path);
    }

    private Object getDefaultFromAnnotation(FieldType type, ConfigsProperty annotation, String fieldName) {
        return switch (type) {
            case STRING -> parseDefaultValue(annotation.defaultString(), type, fieldName);
            case BOOLEAN -> parseDefaultValue(String.valueOf(annotation.defaultBoolean()), type, fieldName);
            case INT -> parseDefaultValue(String.valueOf(annotation.defaultInt()), type, fieldName);
            case DOUBLE -> parseDefaultValue(String.valueOf(annotation.defaultDouble()), type, fieldName);
            default -> null;
        };
    }

    private Object parseDefaultValue(String value, FieldType type, String fieldName) {
        return switch (type) {
            case STRING -> value.isEmpty() ? "default_" + fieldName : value;
            case BOOLEAN -> Boolean.parseBoolean(value);
            case INT -> value.isEmpty() ? 0 : Integer.parseInt(value);
            case DOUBLE -> value.isEmpty() ? 0.0 : Double.parseDouble(value);
            default -> null;
        };
    }

    private Object getValueByType(FieldType type, String path) {
        return switch (type) {
            case STRING -> config.getString(path);
            case BOOLEAN -> config.getBoolean(path);
            case INT -> config.getInt(path);
            case DOUBLE -> config.getDouble(path);
            default -> null;
        };
    }

    public void save() {
        cfgManager.saveConfig(config);
    }
}
