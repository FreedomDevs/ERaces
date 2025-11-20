package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces

class BukkitAbilityLogger(private val plugin: ERaces) : IAbilityLogger {
    override fun info(message: String) = plugin.logger.info(message)
    override fun warn(message: String) = plugin.logger.warning(message)
}