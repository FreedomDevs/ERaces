package dev.elysium.eraces

import dev.elysium.eraces.bootstrap.*
import dev.elysium.eraces.exceptions.internal.InitFailedException
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class ERaces : JavaPlugin() {
    companion object {
        private lateinit var instance: ERaces

        @JvmStatic
        fun logger(): Logger = instance.logger

        @JvmStatic
        fun getInstance(): ERaces = instance
    }

    val context: PluginContext = PluginContext()

    private val initializers = listOf(
        DatabaseInitializer(),
        ManagerInitializer(),
        ConfigInitializer(),
        PlaceholderInitializer(),
        CommandInitializer(),
        ListenerInitializer(),
        LoggerConfigurator()
    )

    private fun runInitializer(init: IInitializer) {
        try {
            init.setup(this)
        } catch (e: Exception) {
            logger.severe(
                if (e is InitFailedException)
                    "Ошибка инициализации ${init.javaClass.simpleName}: ${e.message}"
                else
                    "Неожиданная ошибка в ${init.javaClass.simpleName}: ${e.message}"
            )
            server.pluginManager.disablePlugin(this)
            throw e
        }
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        initializers.forEach { runInitializer(it) }
        logger.info(context.messageManager.data.pluginEnabled)
    }

    override fun onDisable() {
        context.database.close()
        logger.info(context.messageManager.data.pluginDisabled)
    }
}