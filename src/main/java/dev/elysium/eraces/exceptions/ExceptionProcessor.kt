package dev.elysium.eraces.exceptions

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player

object ExceptionProcessor {
    fun process(e: Throwable, context: Any? = null) {
        when (e) {
            is dev.elysium.eraces.exceptions.base.ERacesException -> e.handle()
            else -> {
                val ctxStr = when (context) {
                    is Player -> "Player=${context.name}"
                    null -> ""
                    else -> "Context=$context"
                }
                val msg = "[Unhandled] ${e::class.java.name}: ${e.message} ${if (ctxStr.isNotBlank()) "($ctxStr)" else ""}"
                val stack = e.stackTraceToString()
                ERacesLogger.warning("$msg\n$stack")

                try {
                    val eraces = ERaces.getInstance()
                    if (context is Player && eraces.context.globalConfigManager.data.isDebug) {
                        ChatUtil.message(context, "§c[DEBUG][Unhandled] ${e::class.java.simpleName}: ${e.message}")
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}
