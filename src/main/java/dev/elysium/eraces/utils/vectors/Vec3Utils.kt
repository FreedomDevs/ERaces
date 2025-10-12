package dev.elysium.eraces.utils.vectors

import org.bukkit.entity.LivingEntity
import kotlin.math.cos
import kotlin.math.sin

object Vec3Utils {
    /**
     * Конвертация взгляда сущности в вектор направления (unit).
     */
    fun fromPlayerLook(entity: LivingEntity): Vec3 {
        return fromYawPitch(entity.location.yaw.toDouble(), entity.location.pitch.toDouble())
    }

    /**
     * Создать нормализованный вектор из yaw/pitch (градусы).
     * yaw: поворот по горизонтали (как в Bukkit), pitch: вертикальный (вверх/вниз).
     */
    fun fromYawPitch(yawDeg: Double, pitchDeg: Double): Vec3 {
        val yawRad = Math.toRadians(-yawDeg - 90.0)
        val pitchRad = Math.toRadians(-pitchDeg)
        val x = cos(pitchRad) * cos(yawRad)
        val y = sin(pitchRad)
        val z = cos(pitchRad) * sin(yawRad)
        return Vec3(x, y, z).normalize()
    }

    /**
     * Проверка попадания позиции внутрь приближённой капсулы сущности.
     */
    fun isInsideEntity(entity: LivingEntity, pos: Vec3, radius: Double = 0.3): Boolean {
        val center = Vec3(entity.location.x, entity.location.y + entity.eyeHeight / 2.0, entity.location.z)
        val dx = pos.x - center.x
        val dy = pos.y - center.y
        val dz = pos.z - center.z
        return dx * dx + dy * dy + dz * dz <= radius * radius
    }

    /**
     * Вращение вектора вокруг Y (в градусах), мутирует вектор.
     */
    fun rotateY(vec: Vec3, degrees: Double) {
        val rad = Math.toRadians(degrees)
        val c = cos(rad)
        val s = sin(rad)
        val nx = vec.x * c - vec.z * s
        val nz = vec.x * s + vec.z * c
        vec.x = nx
        vec.z = nz
    }
}