package dev.elysium.eraces.abilities.abils.base

import dev.elysium.eraces.abilities.AbilsManager
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

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
    protected fun resetCooldown(player: Player) {
        AbilsManager.getInstance().clearCooldown(player, id)
    }

    override fun getCooldown(): Long = TimeParser.parseToSeconds(cooldown)
}
