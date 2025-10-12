package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

class RaycastConfig(
    var distance: Double = 30.0,
    var step: Double = 0.3,
    var stopAtBlock: Boolean = true,
    filters: Set<TargetFilter> = emptySet(),
    var onStep: (Location) -> Unit = {},
    var onHit: (LivingEntity) -> Unit = {}
) {
    var filters: Set<TargetFilter> = filters
        set(value) { field = value.toSet() }

    init {
        require(distance > 0) { "Distance must be positive" }
        require(step > 0) { "Step must be positive" }
    }
}
