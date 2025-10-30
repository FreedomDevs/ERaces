package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
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

                if (this is IManaCostAbility) {
                    cfg.set("manaCost", getManaCost())
                }

                if (this is IComboActivatable) {
                    cfg.set("comboKey", getComboKey())
                }

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

            if (this is IManaCostAbility && cfg.contains("manaCost")) {
                val field = this::class.java.getDeclaredField("manaCost")
                field.isAccessible = true
                field.set(this, cfg.getDouble("manaCost"))
            }

            if (this is IComboActivatable && cfg.contains("comboKey")) {
                val field = this::class.java.getDeclaredField("comboKey")
                field.isAccessible = true
                field.set(this, cfg.getString("comboKey"))
            }

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
