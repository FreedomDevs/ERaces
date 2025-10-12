package dev.elysium.eraces.utils.targetUtils.effects

import dev.elysium.eraces.utils.vectors.Vec3
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

/**
 * Источник эффектов:
 * - PLAYER(entity?, offset?, perEntity?) — entity optional: если null берём кастера из Target
 * - LOC(location) — фиксированная локация
 */
sealed class Executor {
    data class PLAYER(val entity: LivingEntity? = null, val offset: Vec3? = null, val perEntity: Boolean = false) : Executor()
    data class LOC(val location: Location) : Executor()
}