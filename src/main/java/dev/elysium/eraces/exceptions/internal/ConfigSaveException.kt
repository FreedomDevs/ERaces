package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class ConfigSaveException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, "CONFIG_SAVE_ERROR", context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(message, "CONFIG_SAVE_ERROR", context, cause)
}