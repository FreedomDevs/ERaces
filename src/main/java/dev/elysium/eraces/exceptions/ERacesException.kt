package dev.elysium.eraces.exceptions


abstract class ERacesException(
    open val code: String = "ERACES_ERROR",
    override val message: String
) : RuntimeException(message) {

    open fun handle() {
        println("[ERaces][$code] $message")
    }
}
