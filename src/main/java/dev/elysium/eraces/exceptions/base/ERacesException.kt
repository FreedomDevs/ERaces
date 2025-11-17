package dev.elysium.eraces.exceptions.base

import dev.elysium.eraces.ERacesLogger
import dev.elysium.eraces.exceptions.ErrorCodes

/**
 * Базовый класс для всех исключений плагина ERaces.
 *
 * Наследуется от [RuntimeException], поэтому может быть выброшен без обязательного перехвата.
 * Предоставляет код ошибки [code] и стандартный метод обработки [handle].
 *
 * @property code код ошибки из [ErrorCodes], используется для логирования и идентификации ошибки
 * @property message сообщение об ошибке, описывающее проблему
 */
abstract class ERacesException(
    open val code: ErrorCodes,
    override val message: String
) : RuntimeException(message) {

    /**
     * Обработка исключения.
     *
     * По умолчанию логирует предупреждение с кодом ошибки и сообщением.
     * Дочерние классы могут переопределять метод для кастомной обработки.
     */
    open fun handle() {
        ERacesLogger.warning("[$code] $message")
    }
}