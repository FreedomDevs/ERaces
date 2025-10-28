package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.IAbility
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.logging.Level

abstract class BaseAbilityWithConfig(override val id: String) : IAbility {

    override fun saveDefaultConfig(plugin: ERaces) {
        runSafe(plugin, "Ошибка при сохранении конфигурации способности $id") {
            val folder = File(plugin.dataFolder, "abils").also { if (!it.exists()) it.mkdirs() }
            val file = File(folder, "$id.yml")
            if (!file.exists()) {
                val cfg = YamlConfiguration()
                writeDefaultParams(cfg)
                cfg.save(file)
            }
        }
    }

    override fun loadConfig(plugin: ERaces) {
        runSafe(plugin, "Ошибка при загрузке конфигурации способности $id") {
            val file = File(plugin.dataFolder, "abils/$id.yml")
            if (!file.exists()) {
                plugin.logger.warning("Файл конфигурации способности $id не найден, будет создан заново")
                saveDefaultConfig(plugin)
            }
            val cfg = YamlConfiguration.loadConfiguration(file)
            loadParams(cfg)
        }
    }

    private fun runSafe(plugin: ERaces, message: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, message, e)
        }
    }
}
