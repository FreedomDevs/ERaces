package dev.elysium.eraces.utils.targetUtils.target

import dev.elysium.eraces.utils.vectors.Vec3
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity

/**
 * Представляет одну точку пересечения при трассировке луча (raycast).
 *
 * @property position Позиция текущей точки трассировки.
 * @property hitBlock Блок, в который попал луч (если есть и он solid).
 * @property hitEntities Список сущностей, задетых лучом в этой точке.
 */
data class RaycastHit(
    val position: Vec3,
    val hitBlock: Block? = null,
    val hitEntities: List<LivingEntity> = emptyList()
)
