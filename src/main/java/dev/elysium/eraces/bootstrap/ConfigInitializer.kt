package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.config.MessageManager
import dev.elysium.eraces.exceptions.internal.InitFailedException

class ConfigInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        try {
            val lang = plugin.context.globalConfigManager.data.lang
            val msgManager = MessageManager(plugin, lang)
            plugin.context.messageManager = msgManager
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при загрузке конфигов", e)
        }
    }
}
