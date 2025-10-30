package dev.elysium.eraces.abilities.interfaces

import dev.elysium.eraces.ERaces
import org.bukkit.configuration.file.YamlConfiguration

interface IConfigurableAbility {
    fun loadParams(cfg: YamlConfiguration)
    fun writeDefaultParams(cfg: YamlConfiguration)

    fun saveDefaultConfig(plugin: ERaces)
    fun loadConfig(plugin: ERaces)
}