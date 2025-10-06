package dev.elysium.eraces.utils.targetUtils.target

import dev.elysium.eraces.utils.CollectionsUtils
import dev.elysium.eraces.utils.targetUtils.shouldCollectEntities
import dev.elysium.eraces.utils.targetUtils.stopsAtBlock
import dev.elysium.eraces.utils.vectors.Vec3
import dev.elysium.eraces.utils.vectors.Vec3Utils
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
    fun raycast(
        player: LivingEntity,
        distance: Double,
        step: Double,
        filters: Set<TargetFilter>
    ): Pair<List<LivingEntity>, List<Block>> {

        val world = player.world
        val eyePos = Vec3(player.location.x, player.location.y + player.eyeHeight, player.location.z)
        val dir = Vec3Utils.fromPlayerLook(player)

        val blockCollector = CollectionsUtils.TargetCollector<Block>()
        val entityCollector = CollectionsUtils.TargetCollector<LivingEntity>()

        var currentPos = eyePos
        val iterations = (distance / step).toInt()

        for (i in 0 until iterations) {
            currentPos += dir * step
            val block = world.getBlockAt(currentPos.x.toInt(), currentPos.y.toInt(), currentPos.z.toInt())
            val dist = currentPos.distance(eyePos)

            if (TargetFilter.BLOCKS in filters) blockCollector.add(block, dist)

            if (filters.stopsAtBlock() && block.isSolid) break

            if (filters.shouldCollectEntities()) {
                for (entity in world.getEntities()) {
                    if (entity !is LivingEntity || entity == player) continue
                    if (entityCollector.contains(entity)) continue
                    if (Vec3Utils.isInsideEntity(entity, currentPos)) {
                        val toEntity = Vec3(
                            entity.location.x - eyePos.x,
                            entity.location.y + entity.eyeHeight - eyePos.y,
                            entity.location.z - eyePos.z
                        )
                        val score = dist - dir.dot(toEntity.normalize())
                        entityCollector.add(entity, score)

                        if (TargetFilter.FIRST_ENTITY in filters) break
                    }
                }
            }
        }

        return entityCollector.getSortedByDistance() to blockCollector.getSortedByDistance()
    }
}