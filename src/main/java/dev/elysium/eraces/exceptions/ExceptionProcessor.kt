package dev.elysium.eraces.exceptions

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.entity.Player

/**
 * Утилитарный объект для централизованной обработки исключений плагина.
 *
 * Используется для логирования необработанных ошибок и вызова методов
 * обработки для пользовательских исключений [dev.elysium.eraces.exceptions.base.ERacesException].
 *
 * Также поддерживает вывод отладочных сообщений игрокам, если включён режим debug.
 */
object ExceptionProcessor {

    /**
     * Обрабатывает исключение [e] в контексте [context].
     *
     * Логика обработки:
     * - Если [e] является наследником [dev.elysium.eraces.exceptions.base.ERacesException], вызывается [dev.elysium.eraces.exceptions.base.ERacesException.handle].
     * - Для всех остальных исключений выводится stacktrace в лог.
     * - Если [context] — игрок и включён debug режим, выводится сообщение игроку.
     *
     * @param e исключение для обработки
     * @param context контекст обработки (например, [Player] или другой объект), по умолчанию `null`
     */
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
                    // Игнорируем любые ошибки при обработке debug-сообщений
                }
            }
        }
    }
}