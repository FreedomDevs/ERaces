package dev.elysium.eraces.datatypes;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class ReflectionUtils {
    private static final Map<FieldType, Function<ConfigurationSection, Function<String, Object>>> TYPE_HANDLERS = new HashMap<>();

    static {
        TYPE_HANDLERS.put(FieldType.INT, section -> section::getInt);
        TYPE_HANDLERS.put(FieldType.DOUBLE, section -> section::getDouble);
        TYPE_HANDLERS.put(FieldType.BOOLEAN, section -> section::getBoolean);
        TYPE_HANDLERS.put(FieldType.LIST, section -> section::getStringList);
        TYPE_HANDLERS.put(FieldType.STRING, section -> section::getString);
        TYPE_HANDLERS.put(FieldType.MAP_STRING_INT, section -> s -> {
            ConfigurationSection mapSection = section.getConfigurationSection(s);
            if (mapSection == null) return null;

            Map<String, Integer> map = new HashMap<>();
            for (String key : mapSection.getKeys(false)) {
                map.put(key, mapSection.getInt(key));
            }
            return map;
        });
    }

    public static void loadSection(Object obj, ConfigurationSection section) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            RaceProperty prop = field.getAnnotation(RaceProperty.class);
            if (prop == null) continue;

            String path = prop.path();
            if (!section.contains(path)) continue;

            Object value;
            if (prop.type() == FieldType.SUBGROUP) {
                value = field.get(obj);
                loadSection(value, section.getConfigurationSection(path));
            } else {
                value = TYPE_HANDLERS.get(prop.type()).apply(section).apply(path);
            }

            field.setAccessible(true);
            field.set(obj, value);
        }
    }
}