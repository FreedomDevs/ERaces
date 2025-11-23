package dev.elysium.eraces

import java.util.logging.Level
import java.util.logging.Logger

object ERacesLogger {
    private val logger: Logger
        get() = ERaces.getInstance().logger

    fun info(message: String) = logger.log(Level.INFO, message)
    fun warning(message: String) = logger.log(Level.WARNING, message)
    fun severe(message: String) = logger.log(Level.SEVERE, message)
    fun debug(message: String) = logger.log(Level.FINE, "[DEBUG] $message")
}