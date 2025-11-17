package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.InternalException

class AbilityRegistrationException(
    message: String,
    cause: Throwable? = null,
    context: Any? = null
) : InternalException(message, "ABILITY_REGISTRATION_ERROR", context, cause)
