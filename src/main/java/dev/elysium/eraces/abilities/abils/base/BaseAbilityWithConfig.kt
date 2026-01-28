package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.internal.ConfigLoadException
import dev.elysium.eraces.exceptions.internal.ConfigParamException
import dev.elysium.eraces.exceptions.internal.ConfigSaveException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.reflect.Field


/**
 * Базовый класс для способностей, которые имеют конфигурацию.
 *
 * Отвечает за сохранение и загрузку конфигурации YAML для каждой способности.
 *
 * @property id уникальный идентификатор способности
 */
abstract class BaseAbilityWithConfig(override val id: String, override val name: String? = null,
                                     override val description: String? = null) : IAbility {

    /**
     * Сохраняет конфигурацию способности по умолчанию.
     * Если файл конфигурации уже существует — ничего не делает.
     * Если способность реализует [IManaCostAbility] или [IComboActivatable],
     * соответствующие значения также сохраняются.
     *
     * @param plugin основной плагин ERaces
     * @throws ConfigSaveException если произошла ошибка при записи конфигурации
     */
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
                    cfg.set("comboKey", getComboKeyy())

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

    /**
     * Загружает конфигурацию способности из файла.
     * Если файл не найден, создаёт дефолтную конфигурацию.
     * Поддерживает автоматическое применение полей [manaCost] и [comboKey],
     * если способность реализует соответствующие интерфейсы.
     *
     * @param plugin основной плагин ERaces
     * @throws ConfigLoadException если произошла ошибка при загрузке конфигурации
     * @throws ConfigParamException если возникла ошибка при применении параметров
     */
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
                    setFieldByNameInHierarchy(this, "manaCost", cfg.getDouble("manaCost"))
                } catch (e: Exception) {
                    throw ConfigParamException(
                        message = "Ошибка применения параметра 'manaCost' для способности '$id': ${e.message}",
                        cause = e,
                        context = file.path
                    )
                }
            }

            // ---- Combo key
            if (this is IComboActivatable && cfg.contains("comboKey")) {
                try {
                    setFieldByNameInHierarchy(this, "comboKey", cfg.getString("comboKey"))
                } catch (e: Exception) {
                    throw ConfigParamException(
                        message = "Ошибка применения параметра 'comboKey' для способности '$id': ${e.message}",
                        cause = e,
                        context = file.path
                    )
                }
            }

            loadParams(cfg)

        } catch (e: ConfigParamException) {
            throw e

        } catch (e: Exception) {
            throw ConfigLoadException(
                message = "Ошибка при загрузке конфигурации способности '$id'",
                cause = e,
                context = file.path
            )
        }
    }

    /**
     * Устанавливает значение приватного или наследуемого поля по имени,
     * обходя всю иерархию классов.
     *
     * Используется для автоматического применения конфигурационных параметров.
     *
     * @param target объект, в котором ищется поле
     * @param fieldName имя поля
     * @param value значение для установки
     * @throws Exception если поле не найдено или не удалось установить значение
     */
    private fun setFieldByNameInHierarchy(target: Any, fieldName: String, value: Any?) {
        var cls: Class<*>? = target::class.java
        var lastEx: Exception? = null

        while (cls != null) {
            try {
                val field: Field = cls.getDeclaredField(fieldName)
                field.isAccessible = true
                val fieldType = field.type
                val toSet = when {
                    value == null -> null
                    fieldType == java.lang.Double.TYPE || fieldType == java.lang.Double::class.java -> (value as Number).toDouble()
                    fieldType == java.lang.Long.TYPE || fieldType == java.lang.Long::class.java -> (value as Number).toLong()
                    fieldType == java.lang.Integer.TYPE || fieldType == java.lang.Integer::class.java -> (value as Number).toInt()
                    fieldType == java.lang.Float.TYPE || fieldType == java.lang.Float::class.java -> (value as Number).toFloat()
                    fieldType == java.lang.Boolean.TYPE || fieldType == java.lang.Boolean::class.java -> value as Boolean
                    fieldType == java.lang.String::class.java -> value.toString()
                    else -> value
                }
                field.set(target, toSet)
                return
            } catch (ex: NoSuchFieldException) {
                cls = cls.superclass
                continue
            } catch (ex: Exception) {
                lastEx = ex
                break
            }
        }

        throw lastEx ?: NoSuchFieldException("Поле '$fieldName' не найдено в классе ${target::class.java.name} и его суперклассах")
    }
}
