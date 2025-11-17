package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка при сохранении конфигурационного файла.
 */
class ConfigSaveException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.CONFIG_SAVE, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        ErrorCodes.CONFIG_SAVE,
        context,
        cause
    )
}