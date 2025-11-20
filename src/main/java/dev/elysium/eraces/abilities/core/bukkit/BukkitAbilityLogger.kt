package dev.elysium.eraces.abilities.core.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.IAbilityLogger

class BukkitAbilityLogger(private val plugin: ERaces) : IAbilityLogger {
    override fun info(message: String) = plugin.logger.info(message)
    override fun warn(message: String) = plugin.logger.warning(message)
}