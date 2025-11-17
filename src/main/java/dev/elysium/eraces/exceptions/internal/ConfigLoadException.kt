package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class ConfigLoadException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, "CONFIG_LOAD_ERROR", context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(
        message,
        "CONFIG_LOAD_ERROR",
        context,
        cause
    )
}