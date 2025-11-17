package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка при загрузке конфигурационного файла.
 */
class ConfigLoadException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.CONFIG_LOAD, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        ErrorCodes.CONFIG_LOAD,
        context,
        cause
    )
}