package dev.elysium.eraces.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.elysium.eraces.ERaces;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

public class MessageManager {

    public MessageManager() {
        File langDir = new File(ERaces.getInstance().getDataFolder(), "lang");
        if (!langDir.exists()) {
            langDir.mkdirs();
            ERaces.getInstance().saveResource("lang/en_US.properties", false);
            ERaces.getInstance().saveResource("lang/ru_RU.properties", false);
        }

        loadMessage();
    }

    private void loadMessage() {
        File langDir = new File(ERaces.getInstance().getDataFolder(), "lang");
        if (!langDir.exists()) return;

        TranslationStore.StringBased<MessageFormat> store =
                TranslationStore.messageFormat(Key.key("elysium:eraces"));

        for (File file : langDir.listFiles()) {
            if (!file.getName().endsWith(".json")) continue;
            try {
                Properties props = jsonToProperties(file);

                String[] parts = file.getName().replace(".json", "").split("_");
                Locale locale = new Locale(parts[0], parts.length > 1 ? parts[1] : "");

                ResourceBundle bundle = new PropertiesResourceBundle(props);

                store.registerAll(locale, bundle, true);

                ERaces.getInstance().getLogger().info(LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("translations.reloaded"), getDefaultLocale())));
            } catch (Exception e) {
                ERaces.getInstance().getLogger().warning("Failed to reload " + file.getName() + ": " + e.getMessage());
            }
        }

        GlobalTranslator.translator().addSource(store);
    }

    private Locale getDefaultLocale(){
        return Locale.of(ERaces.getGlobalCfg().getData().getDefaultLang());
    }

    public Component getTranslation(String key, @Nullable CommandSender commandSender, Component... args) {
        Component translatable = Component.translatable(key, args);

        switch (ERaces.getGlobalCfg().getData().getTranslator()) {
            case "resource_pack":
                return translatable;
            case "paper":
                if (commandSender == null)
                    return GlobalTranslator.render(translatable, getDefaultLocale());

                if (commandSender instanceof Player player)
                    return GlobalTranslator.render(translatable, player.locale());

                return GlobalTranslator.render(translatable, getDefaultLocale());
            case "only_default":
                return GlobalTranslator.render(translatable, getDefaultLocale());
            default:
                return GlobalTranslator.render(translatable, getDefaultLocale());
        }
    }

    public String getLogTranslation(String key, Component... args) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(getTranslation(key, null, args));
    }

    public Component[] stringsToComponents(String... strings) {
        MiniMessage mm = MiniMessage.miniMessage();
        Component[] components = new Component[strings.length];
        for (int i = 0; i < strings.length; i++) {
            components[i] = mm.deserialize(strings[i]);
        }
        return components;
    }

    public String getLogTranslation(String key, String... args) {
        return getLogTranslation(key, stringsToComponents(args));
    }

    private Properties jsonToProperties(File jsonFile) throws Exception {
        try (Reader reader = new FileReader(jsonFile)) {
            Type mapType = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> map = new Gson().fromJson(reader, mapType);

            Properties props = new Properties();
            props.putAll(map);

            return props;
        }
    }

    private static class PropertiesResourceBundle extends ResourceBundle {
        private final Properties props;
        public PropertiesResourceBundle(Properties props) {
            this.props = props;
        }
        @Override
        protected Object handleGetObject(String key) {
            return props.getProperty(key);
        }
        @Override
        public boolean containsKey(String key) {
            return props.containsKey(key);
        }
        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(props.stringPropertyNames());
        }
    }

    public void reload() {
        loadMessage();
    }
}