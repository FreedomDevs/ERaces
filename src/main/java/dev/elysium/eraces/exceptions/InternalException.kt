package dev.elysium.eraces.exceptions

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.ChatUtil

abstract class InternalException(
    override val message: String,
    override val code: String = "INTERNAL_ERROR",
    val context: Any? = null,
    cause: Throwable? = null
) : ERacesException(code, message) {

    init {
        if (cause != null) initCause(cause)
    }

    override fun handle() {
        val ctx = context?.let { " Context: $it" } ?: ""
        println("[ERaces][Internal][$code] $message$ctx")

        if (ERaces.getInstance().context.globalConfigManager.data.isDebug) {
            ERaces.getInstance().server.onlinePlayers
                .filter { it.hasPermission("eraces.admin") }
                .forEach { ChatUtil.message(it, "§c[DEBUG][Internal] $message") }
        }
    }
}