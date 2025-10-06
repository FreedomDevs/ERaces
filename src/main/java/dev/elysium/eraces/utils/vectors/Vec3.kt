package dev.elysium.eraces.utils.vectors

import kotlin.math.sqrt

data class Vec3(val x: Double, val y: Double, val z: Double) {
    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Double) = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Double) = Vec3(x / scalar, y / scalar, z / scalar)

    fun length(): Double = sqrt(x * x + y * y + z * z)

    fun normalize(): Vec3 {
        val len = length()
        return if (len != 0.0) this / len else Vec3(0.0, 0.0, 0.0)
    }

    fun dot(other: Vec3) = x * other.x + y * other.y + z * other.z
    fun cross(other: Vec3) = Vec3(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    fun distance(other: Vec3) = (this - other).length()
    fun lerp(other: Vec3, t: Double) = this * (1 - t) + other * t

    fun isInsideSphere(center: Vec3, radius: Double): Boolean {
        val dx = x - center.x
        val dy = y - center.y
        val dz = z - center.z
        return dx * dx + dy * dy + dz * dz <= radius * radius
    }
}