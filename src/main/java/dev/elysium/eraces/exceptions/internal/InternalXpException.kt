package dev.elysium.eraces.exceptions.internal

import dev.elysium.eraces.exceptions.ErrorCodes
import dev.elysium.eraces.exceptions.base.InternalException

/**
 * Ошибка, связанная с внутренней обработкой опыта (XP).
 */
class InternalXpException : InternalException {
    constructor(message: String, context: Any? = null) : super(message, ErrorCodes.XP_INTERNAL_ERROR, context)
    constructor(message: String, cause: Throwable, context: Any? = null) : super(message, ErrorCodes.XP_INTERNAL_ERROR, context, cause)
}