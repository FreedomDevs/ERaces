package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.exceptions.InitFailedException;

import java.util.logging.Level;

public class LoggerConfigurator implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        try {
            if (plugin.getContext().getGlobalConfigManager().getData().isDebug()) {
                plugin.getLogger().setLevel(Level.FINE);
                plugin.getLogger().info("Режим отладки включён.");
            }
        } catch (Exception e) {
            throw new InitFailedException("Ошибка при настройке логирования", e);
        }
    }
}
