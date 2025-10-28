package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration

/**
 * Базовый класс для способностей для работы с кулдаунами.
 */
abstract class BaseCooldownAbility(
    id: String,
    private val defaultCooldown: String = "10s"
) : BaseAbility(id), ICooldownAbility {

    private var cooldown: String = defaultCooldown

    final override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getString("cooldown", defaultCooldown)!!
        loadCustomParams(cfg)
    }

    final override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", cooldown)
        writeCustomDefaults(cfg)
    }

    protected open fun loadCustomParams(cfg: YamlConfiguration) {}
    protected open fun writeCustomDefaults(cfg: YamlConfiguration) {}

    override fun getCooldown(): Long = TimeParser.parseToSeconds(cooldown)
}
