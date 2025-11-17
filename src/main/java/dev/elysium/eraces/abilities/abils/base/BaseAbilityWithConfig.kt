package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.internal.ConfigParamException
import dev.elysium.eraces.exceptions.internal.ConfigSaveException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.logging.Level

abstract class BaseAbilityWithConfig(override val id: String) : IAbility {

    override fun saveDefaultConfig(plugin: ERaces) {
        val folder = File(plugin.dataFolder, "abils").also { it.mkdirs() }
        val file = File(folder, "$id.yml")

        if (!file.exists()) {
            try {
                val cfg = YamlConfiguration()

                writeDefaultParams(cfg)

                if (this is IManaCostAbility)
                    cfg.set("manaCost", getManaCost())

                if (this is IComboActivatable)
                    cfg.set("comboKey", getComboKey())

                cfg.save(file)

            } catch (e: Exception) {
                throw ConfigSaveException(
                    message = "Ошибка при сохранении конфигурации способности '$id'",
                    cause = e,
                    context = file.path
                )
            }
        }
    }

    override fun loadConfig(plugin: ERaces) {
        val file = File(plugin.dataFolder, "abils/$id.yml")

        try {
            if (!file.exists()) {
                ERacesLogger.warning("Файл конфигурации способности $id не найден, создаю дефолтный")
                saveDefaultConfig(plugin)
            }

            val cfg = YamlConfiguration.loadConfiguration(file)

            // ---- Mana cost
            if (this is IManaCostAbility && cfg.contains("manaCost")) {
                try {
                    val field = this::class.java.getDeclaredField("manaCost")
                    field.isAccessible = true
                    field.set(this, cfg.getDouble("manaCost"))
                } catch (e: Exception) {
                    throw ConfigParamException(
                        message = "Ошибка применения параметра 'manaCost' для способности '$id'",
                        cause = e,
                        context = file.path
                    )
                }
            }

            // ---- Combo key
            if (this is IComboActivatable && cfg.contains("comboKey")) {
                try {
                    val field = this::class.java.getDeclaredField("comboKey")
                    field.isAccessible = true
                    field.set(this, cfg.getString("comboKey"))
                } catch (e: Exception) {
                    throw ConfigParamException(
                        message = "Ошибка применения параметра 'comboKey' для способности '$id'",
                        cause = e,
                        context = file.path
                    )
                }
            }

            loadParams(cfg)

        } catch (e: ConfigParamException) {
            throw e

        } catch (e: Exception) {
            throw ConfigSaveException(
                message = "Ошибка при загрузке конфигурации способности '$id'",
                cause = e,
                context = file.path
            )
        }
    }
}
