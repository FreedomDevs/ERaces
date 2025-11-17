package dev.elysium.eraces.abilities


import org.bukkit.configuration.file.YamlConfiguration
import kotlin.reflect.KMutableProperty0

/**
 * Утилитарный объект для упрощённой работы с YAML-конфигурацией способностей.
 *
 * Позволяет:
 * - читать значения из конфигурации в свойства с использованием Kotlin Reflection,
 * - записывать значения в конфигурацию.
 *
 * Пример использования:
 * ```
 * ConfigHelper.with(cfg) {
 *     read("cooldown", ::cooldown)
 *     write("manaCost", 50)
 * }
 * ```
 */
object ConfigHelper {

    /** Внутреннее хранилище YAML-конфигурации для текущего блока [with] */
    private lateinit var cfg: YamlConfiguration

    /**
     * Выполняет блок с заданной конфигурацией [cfg].
     * Все вызовы [read] и [write] внутри блока будут использовать эту конфигурацию.
     *
     * @param cfg YAML-конфигурация
     * @param block блок кода с вызовами [read] и [write]
     */
    fun with(cfg: YamlConfiguration, block: ConfigHelper.() -> Unit) {
        this.cfg = cfg
        this.block()
    }

    /**
     * Читает значение из конфигурации по ключу [key] и устанавливает его в свойство [prop].
     * Тип значения определяется автоматически по текущему типу свойства.
     *
     * Поддерживаемые типы: [String], [Double], [Int], [Long], [Boolean].
     *
     * @param key ключ в YAML-конфигурации
     * @param prop ссылка на изменяемое свойство
     */
    @Suppress("UNCHECKED_CAST")
    fun read(key: String, prop: KMutableProperty0<*>) {
        when (val current = prop.get()) {
            is String -> prop as KMutableProperty0<String> then { it.set(cfg.getString(key, current)!!) }
            is Double -> prop as KMutableProperty0<Double> then { it.set(cfg.getDouble(key, current)) }
            is Int -> prop as KMutableProperty0<Int> then { it.set(cfg.getInt(key, current)) }
            is Long -> prop as KMutableProperty0<Long> then { it.set(cfg.getLong(key, current)) }
            is Boolean -> prop as KMutableProperty0<Boolean> then { it.set(cfg.getBoolean(key, current)) }
        }
    }

    /**
     * Вспомогательный инфиксный метод для цепочного применения действий к [KMutableProperty0].
     */
    private inline infix fun <T> KMutableProperty0<T>.then(action: (KMutableProperty0<T>) -> Unit) = action(this)

    /**
     * Записывает значение [value] в конфигурацию по ключу [key].
     *
     * @param key ключ в YAML-конфигурации
     * @param value значение для записи
     */
    fun write(key: String, value: Any) {
        cfg.set(key, value)
    }
}