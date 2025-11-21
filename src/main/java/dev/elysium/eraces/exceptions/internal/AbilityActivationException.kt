package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка при активации способности.
 */
class AbilityActivationException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.ABILITY_ACTIVATION, context)
    constructor(message: String, cause: Throwable?, context: Any? = null) : super(
        message,
        ErrorCodes.ABILITY_ACTIVATION,
        context,
        cause
    )
}