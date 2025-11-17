package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка применения параметров конфигурации способности (рефлексия, неверные поля).
 */
class ConfigParamException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.CONFIG_PARAM, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        ErrorCodes.CONFIG_PARAM,
        context,
        cause
    )
}