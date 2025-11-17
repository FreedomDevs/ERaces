package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class InternalXpException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, "XP_INTERNAL_ERROR", context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(message, "XP_INTERNAL_ERROR", context, cause)
}