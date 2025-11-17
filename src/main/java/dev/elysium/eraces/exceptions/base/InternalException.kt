package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.utils.ChatUtil

/**
 * Базовое исключение для внутренних ошибок плагина ERaces.
 *
 * Используется для ошибок, связанных с некорректной работой плагина,
 * не зависящих от действий игрока.
 *
 * @property code код ошибки из [ErrorCodes], по умолчанию [ErrorCodes.INTERNAL_ERROR]
 * @property context дополнительный контекст, например путь к файлу или объект, вызывающий ошибку
 * @param message сообщение об ошибке
 * @param cause причина ошибки, если есть
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

    /**
     * Обработка внутреннего исключения.
     *
     * Логирует предупреждение с кодом ошибки, сообщением, контекстом и причиной.
     * Если включён debug-режим, отправляет сообщение всем онлайн-администраторам.
     *
     * Метод можно переопределять в дочерних классах для кастомной обработки.
     */
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
