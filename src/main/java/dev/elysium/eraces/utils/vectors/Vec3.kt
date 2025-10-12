package dev.elysium.eraces.utils.vectors

import kotlin.math.sqrt

data class Vec3(var x: Double, var y: Double, var z: Double) {
    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Double) = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Double) = Vec3(x / scalar, y / scalar, z / scalar)

    fun length(): Double = sqrt(x * x + y * y + z * z)

    fun normalize(): Vec3 {
        val len = length()
        return if (len != 0.0) this / len else Vec3(0.0, 0.0, 0.0)
    }

    fun distance(other: Vec3) = (this - other).length()
}
