package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

interface IAbility {
    val id: String

    fun activate(player: Player)

    fun loadParams(cfg: YamlConfiguration)

    fun saveDefaultConfig(plugin: ERaces) {
        val folder = File(plugin.dataFolder, "abils")
        if (!folder.exists()) folder.mkdirs()

        val file = File(folder, "$id.yml")
        if (!file.exists()) {
            val cfg = YamlConfiguration()
            writeDefaultParams(cfg)
            cfg.save(file)
        }
    }

    fun loadConfig(plugin: ERaces) {
        val file = File(plugin.dataFolder, "abils/$id.yml")
        val cfg = YamlConfiguration.loadConfiguration(file)
        loadParams(cfg)
    }

    fun writeDefaultParams(cfg: YamlConfiguration)

}