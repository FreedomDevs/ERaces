package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.exceptions.internal.InitFailedException;

public class ConfigInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        try {
            String lang = plugin.getContext().getGlobalConfigManager().getData().getLang();
            MessageManager msgManager = new MessageManager(plugin, lang);
            plugin.getContext().setMessageManager(msgManager);
        } catch (Exception e) {
            throw new InitFailedException("Ошибка при загрузке конфигов", e);
        }
    }
}
