package dev.elysium.eraces.exceptions

class InitFailedException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
