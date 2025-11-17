package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERacesLogger

abstract class ERacesException(
    open val code: String,
    override val message: String
) : RuntimeException(message) {

    open fun handle() {
        ERacesLogger.warning("[$code] $message")
    }
}
