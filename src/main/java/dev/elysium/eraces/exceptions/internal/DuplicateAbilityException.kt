package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка: попытка зарегистрировать способность, которая уже существует.
 */
class DuplicateAbilityException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.ABILITY_DUPLICATE, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        ErrorCodes.ABILITY_DUPLICATE,
        context,
        cause
    )
}