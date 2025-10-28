package dev.elysium.eraces.abilities


import org.bukkit.configuration.file.YamlConfiguration
import kotlin.reflect.KMutableProperty0

object ConfigHelper {
    private lateinit var cfg: YamlConfiguration

    fun with(cfg: YamlConfiguration, block: ConfigHelper.() -> Unit) {
        this.cfg = cfg
        this.block()
    }

    fun read(key: String, prop: KMutableProperty0<*>) {
        when (val current = prop.get()) {
            is String -> prop as KMutableProperty0<String> then { it.set(cfg.getString(key, current)!!) }
            is Double -> prop as KMutableProperty0<Double> then { it.set(cfg.getDouble(key, current)) }
            is Int -> prop as KMutableProperty0<Int> then { it.set(cfg.getInt(key, current)) }
            is Long -> prop as KMutableProperty0<Long> then { it.set(cfg.getLong(key, current)) }
            is Boolean -> prop as KMutableProperty0<Boolean> then { it.set(cfg.getBoolean(key, current)) }
        }
    }

    private inline infix fun <T> KMutableProperty0<T>.then(action: (KMutableProperty0<T>) -> Unit) = action(this)

    fun write(key: String, value: Any) {
        cfg.set(key, value)
    }
}

