package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка при регистрации способности.
 */
class AbilityRegistrationException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.ABILITY_REGISTRATION, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        ErrorCodes.ABILITY_REGISTRATION,
        context,
        cause
    )
}
