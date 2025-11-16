package dev.elysium.eraces

import dev.elysium.eraces.abilities.AbilsManager
import dev.elysium.eraces.bootstrap.*
import dev.elysium.eraces.exceptions.InitFailedException
import dev.elysium.eraces.gui.raceSelect.RaceSelectMenuPages
import dev.elysium.eraces.utils.targetUtils.PluginAccessor
import org.bukkit.plugin.java.JavaPlugin

class ERaces : JavaPlugin() {
    companion object {
        private lateinit var instance: ERaces

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
        } catch (e: InitFailedException) {
            logger.severe("Ошибка инициализации ${init.javaClass.simpleName}: ${e.message}")
            server.pluginManager.disablePlugin(this)
            throw e
        } catch (e: Exception) {
            logger.severe("Неожиданная ошибка в ${init.javaClass.simpleName}")
            server.pluginManager.disablePlugin(this)
            throw e
        }
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        RaceSelectMenuPages.registerDefaults();
        RacesReloader.startListeners(this)

        initializers.forEach { runInitializer(it) }

        AbilsManager.init(this)
        PluginAccessor.init(this)

        logger.info(context.messageManager.data.pluginEnabled)
    }

    override fun onDisable() {
        logger.info(context.messageManager.data.pluginDisabled);
        context.database.close();
    }
}