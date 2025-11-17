package dev.elysium.eraces

import java.util.logging.Level
import java.util.logging.Logger

object ERacesLogger {
    private val logger: Logger
        get() = ERaces.getInstance().logger

    fun info(message: String) = logger.log(Level.INFO, "[ERaces] $message")
    fun warning(message: String) = logger.log(Level.WARNING, "[ERaces] $message")
    fun severe(message: String) = logger.log(Level.SEVERE, "[ERaces] $message")

    fun debug(message: String) {
        if (ERaces.getInstance().context.globalConfigManager.data.isDebug) {
            logger.log(Level.INFO, "[ERaces][DEBUG] $message")
        }
    }
}