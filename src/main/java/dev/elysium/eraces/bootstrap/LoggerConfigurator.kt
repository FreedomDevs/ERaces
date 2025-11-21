package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERaces.Companion.logger
import dev.elysium.eraces.exceptions.internal.InitFailedException
import java.util.logging.Level

class LoggerConfigurator : IInitializer {
    override fun setup(plugin: ERaces) {
        try {
            if (plugin.context.globalConfigManager.getData().isDebug()) {
                logger().level = Level.FINE
                logger().info("Режим отладки включён.")
            }
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при настройке логирования", e)
        }
    }
}
