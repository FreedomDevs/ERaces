package dev.elysium.eraces.utils.targetUtils.target

import dev.elysium.eraces.utils.vectors.Vec3
import dev.elysium.eraces.utils.vectors.Vec3Utils
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity

/**
 * НЕ ВЛЕЗАЙ, УБЬЁТ (L1nkolb154 я тебе XD)
 *
 * Этот код проверяет блоки и сущности, считает дистанции и сортирует цели.
 * Не трогайте его без крайней необходимости — используйте Target и его методы.
 * Работает на божьей помощи.
 */
object TargetRaycast {

    fun raycast(origin: Vec3, direction: Vec3, distance: Double, step: Double, world: World): List<RaycastHit> {
        val hits = mutableListOf<RaycastHit>()
        var current = origin.copy()
        val iterations = (distance / step).toInt().coerceAtLeast(1)

        repeat(iterations) {
            current = advance(current, direction, step)
            val block = getBlockAt(world, current)
            val entities = getEntitiesAt(world, current)
            hits.add(RaycastHit(current.copy(), block?.takeIf { it.type.isSolid }, entities))
        }

        return hits
    }

    // ----------------- helpers -----------------

    private fun advance(pos: Vec3, dir: Vec3, step: Double): Vec3 =
        Vec3(pos.x + dir.x * step, pos.y + dir.y * step, pos.z + dir.z * step)

    private fun getBlockAt(world: World, pos: Vec3): Block? =
        try { world.getBlockAt(pos.x.toInt(), pos.y.toInt(), pos.z.toInt()) }
        catch (_: Exception) { null }

    private fun getEntitiesAt(world: World, pos: Vec3, radius: Double = 0.3): List<LivingEntity> =
        world.entities.filterIsInstance<LivingEntity>().filter { Vec3Utils.isInsideEntity(it, pos, radius) }
}
