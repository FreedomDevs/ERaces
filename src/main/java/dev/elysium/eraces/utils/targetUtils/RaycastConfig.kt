package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

/**
 * Конфигурация для выполнения raycast.
 * Хранит все параметры трассировки луча, фильтры и колбэки.
 *
 * @property distance Максимальная дальность raycast (в блоках). Должно быть > 0.
 * @property step Шаг движения луча за одну итерацию. Должно быть > 0.
 * @property stopAtBlock Если true, raycast остановится при столкновении с первым твердым блоком.
 * @property filters Набор фильтров TargetFilter, которые определяют поведение raycast (например, учитывать сущности, блоки и т.д.).
 * @property onStep Lambda-функция, вызываемая на каждом шаге трассировки луча.
 * @property onHit Lambda-функция, вызываемая при попадании в сущность.
 */
class RaycastConfig(
    var distance: Double = 30.0,
    var step: Double = 0.3,
    var stopAtBlock: Boolean = true,
    filters: Set<TargetFilter> = emptySet(),
    var onStep: (Location) -> Unit = {},
    var onHit: (LivingEntity) -> Unit = {}
) {
    /**
     * Набор фильтров TargetFilter.
     * Устанавливается через setter с копированием в новый Set для иммутабельности.
     */
    var filters: Set<TargetFilter> = filters
        set(value) {
            field = value.toSet()
        }

    init {
        require(distance > 0) { "Distance must be positive" }
        require(step > 0) { "Step must be positive" }
    }
}
