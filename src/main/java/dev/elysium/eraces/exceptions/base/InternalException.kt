package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.utils.ChatUtil

/**
 * Базовое исключение для внутренних ошибок плагина.
 * @param message Сообщение об ошибке
 * @param code Код ошибки
 * @param context Дополнительный контекст, если есть
 * @param cause Причина ошибки, если есть
 */
abstract class InternalException(
    message: String,
    override val code: ErrorCodes = ErrorCodes.INTERNAL_ERROR,
    val context: Any? = null,
    cause: Throwable? = null
) : ERacesException(code, message) {

    init {
        cause?.let { initCause(it) }
    }

    override fun handle() {
        val ctx = context?.let { " Context: $it" } ?: ""
        val causeInfo =
            cause?.let { "\nCause: ${it::class.java.name}: ${it.message}\n${it.stackTraceToString()}" } ?: ""
        ERacesLogger.warning("[Internal][$code] $message$ctx$causeInfo")

        try {
            val eraces = ERaces.getInstance()
            if (eraces.context.globalConfigManager.data.isDebug) {
                eraces.server.onlinePlayers
                    .filter { it.hasPermission("eraces.admin") }
                    .forEach { ChatUtil.message(it, "§c[DEBUG][Internal] $message") }
            }
        } catch (ignored: Exception) {
        }
    }
}
