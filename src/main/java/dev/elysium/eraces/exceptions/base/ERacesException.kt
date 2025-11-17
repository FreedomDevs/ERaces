package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.exceptions.ErrorCodes

abstract class ERacesException(
    open val code: ErrorCodes,
    override val message: String
) : RuntimeException(message) {

    open fun handle() {
        ERacesLogger.warning("[$code] $message")
    }
}
