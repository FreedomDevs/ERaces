package dev.elysium.eraces.utils.eParticle

import org.bukkit.Particle
import org.bukkit.util.Vector

data class ParticleConfig(
    val particle: Particle,
    val count: Int,
    val offsetX: Double,
    val offsetY: Double,
    val offsetZ: Double,
    val extra: Double,
    val direction: Vector?,
    val distance: Double?,
    val step: Double
) {
    class Builder(val particle: Particle) {
        var count: Int = 1
        var offsetX: Double = 0.0
        var offsetY: Double = 0.0
        var offsetZ: Double = 0.0
        var extra: Double = 0.0
        var direction: Vector? = null
        var distance: Double? = null
        var step: Double = 0.2

        fun count(count: Int) = apply { this.count = count }
        fun offset(x: Double, y: Double, z: Double) = apply { offsetX = x; offsetY = y; offsetZ = z }
        fun extra(extra: Double) = apply { this.extra = extra }
        fun direction(direction: Vector) = apply { this.direction = direction }
        fun distance(distance: Double) = apply { this.distance = distance }
        fun step(step: Double) = apply { this.step = step }
        fun build() = ParticleConfig(particle, count, offsetX, offsetY, offsetZ, extra, direction, distance, step)
    }
}

