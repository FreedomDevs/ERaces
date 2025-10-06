package dev.elysium.eraces.utils.vectors

import org.bukkit.entity.LivingEntity
import kotlin.math.cos
import kotlin.math.sin

object Vec3Utils {
    fun fromPlayerLook(player: LivingEntity): Vec3 {
        val yawRad = Math.toRadians((-player.location.yaw - 90).toDouble())
        val pitchRad = Math.toRadians((-player.location.pitch).toDouble())
        val x = cos(pitchRad) * cos(yawRad)
        val y = sin(pitchRad)
        val z = cos(pitchRad) * sin(yawRad)
        return Vec3(x, y, z).normalize()
    }

    fun isInsideEntity(entity: LivingEntity, pos: Vec3, radius: Double = 0.3): Boolean {
        val center = Vec3(
            entity.location.x,
            entity.location.y + entity.height / 2,
            entity.location.z
        )
        val dx = pos.x - center.x
        val dy = pos.y - center.y
        val dz = pos.z - center.z
        return dx * dx + dy * dy + dz * dz <= radius * radius
    }
}