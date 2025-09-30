package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.logging.Level

interface IAbility {
    val id: String

    fun activate(player: Player)

    fun loadParams(cfg: YamlConfiguration)

    fun saveDefaultConfig(plugin: ERaces) {
        try {
            val folder = File(plugin.dataFolder, "abils")
            if (!folder.exists() && !folder.mkdirs()) {
                plugin.logger.warning("Не удалось создать папку для способностей: ${folder.path}")
                return
            }

            val file = File(folder, "$id.yml")
            if (!file.exists()) {
                val cfg = YamlConfiguration()
                writeDefaultParams(cfg)
                cfg.save(file)
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Ошибка при сохранении конфигурации способности $id", e)
        }
    }

    fun loadConfig(plugin: ERaces) {
        try {
            val file = File(plugin.dataFolder, "abils/$id.yml")
            if (!file.exists()) {
                plugin.logger.warning("Файл конфигурации способности $id не найден, будет создан заново")
                saveDefaultConfig(plugin)
            }
            val cfg = YamlConfiguration.loadConfiguration(file)
            loadParams(cfg)
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Ошибка при загрузке конфигурации способности $id", e)
        }
    }

    fun writeDefaultParams(cfg: YamlConfiguration)

}