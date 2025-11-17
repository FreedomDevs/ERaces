package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

/**
 * Ошибка инициализации чего-либо в плагине (например, конфигов, сервисов, менеджеров).
 *
 * Используется для критических внутренних ошибок.
 */
class InitFailedException : InternalException {
    constructor(message: String) : super(message = message)
    constructor(message: String, cause: Throwable) : super(message = message, code = "INIT_FAILED", cause = cause)
    constructor(cause: Throwable) : super(
        message = cause.message ?: "Неизвестная ошибка",
        code = "INIT_FAILED",
        cause = cause
    )
}
