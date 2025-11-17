package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class AbilityActivationException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, "ABILITY_ACTIVATION_ERROR", context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        "ABILITY_ACTIVATION_ERROR",
        context,
        cause
    )
}