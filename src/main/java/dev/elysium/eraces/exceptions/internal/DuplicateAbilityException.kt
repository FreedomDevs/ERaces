package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class DuplicateAbilityException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, "ABILITY_DUPLICATE", context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        "ABILITY_DUPLICATE",
        context,
        cause
    )
}