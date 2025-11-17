package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка инициализации плагина (например, конфигов или сервисов).
 */
class InitFailedException : InternalException {
    constructor(message: String) : super(message, ErrorCodes.INIT_FAILED)
    constructor(message: String, cause: Throwable) : super(message, ErrorCodes.INIT_FAILED, null, cause)
    constructor(cause: Throwable) : super(cause.message ?: "Неизвестная ошибка", ErrorCodes.INIT_FAILED, null, cause)
}