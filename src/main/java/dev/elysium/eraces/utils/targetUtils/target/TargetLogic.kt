package dev.elysium.eraces.utils.targetUtils.target

import dev.elysium.eraces.utils.CollectionsUtils
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.vectors.KDTree
import dev.elysium.eraces.utils.vectors.Vec3
import dev.elysium.eraces.utils.vectors.Vec3Utils
import org.bukkit.entity.LivingEntity

object TargetLogic {

    fun eyeRaycast(caster: LivingEntity?, filters: Set<TargetFilter>, distance: Double, step: Double): Target {
        caster ?: return Target.fromNull()
        val origin = eyePosition(caster)
        val dir = Vec3Utils.fromPlayerLook(caster)
        val hits = TargetRaycast.raycast(origin, dir, distance, step, caster.world)
        return processHits(caster, filters, hits)
    }

    fun radiusSearch(
        caster: LivingEntity?, filters: Set<TargetFilter>, radius: Double,
        useRaycast: Boolean, step: Double, rays: Int = 16
    ): Target {
        caster ?: return Target.fromNull()
        return if (!useRaycast) nonRaycastSearch(caster, filters, radius)
        else raycastRadiusSearch(caster, filters, radius, step, rays)
    }

    fun kdTreeRadiusSearch(caster: LivingEntity, filters: Set<TargetFilter>, radius: Double): List<LivingEntity> {
        val worldEntities = caster.world.entities.filterIsInstance<LivingEntity>()
        val points = worldEntities.map { it to Vec3(it.location.x, it.location.y, it.location.z) }
        val tree = KDTree(points.map { it.second })
        val center = Vec3(caster.location.x, caster.location.y, caster.location.z)
        val inRadiusVecs = tree.queryRadius(center, radius)
        return inRadiusVecs.mapNotNull { vec -> points.find { it.second == vec }?.first }
            .filter { it.uniqueId != caster.uniqueId }
    }

    // ----------------- helpers -----------------

    private fun eyePosition(caster: LivingEntity) =
        Vec3(caster.location.x, caster.location.y + caster.eyeHeight, caster.location.z)

    private fun nonRaycastSearch(caster: LivingEntity, filters: Set<TargetFilter>, radius: Double): Target {
        val nearby = caster.world.getNearbyEntities(caster.location, radius, radius, radius)
            .filterIsInstance<LivingEntity>()
            .filter { it.uniqueId != caster.uniqueId }
        return Target(nearby, emptyList(), caster, filters)
    }

    private fun raycastRadiusSearch(caster: LivingEntity, filters: Set<TargetFilter>, radius: Double, step: Double, rays: Int): Target {
        val origin = eyePosition(caster)
        val allHits = mutableListOf<RaycastHit>()
        val pitchSteps = rays
        val yawSteps = rays

        for (pi in 0 until pitchSteps) {
            val pitch = -90.0 + pi * (180.0 / (pitchSteps - 1).coerceAtLeast(1))
            for (yi in 0 until yawSteps) {
                val yaw = yi * (360.0 / yawSteps)
                val dir = Vec3Utils.fromYawPitch(yaw, pitch)
                allHits += TargetRaycast.raycast(origin, dir, radius, step, caster.world)
            }
        }
        return processHits(caster, filters, allHits)
    }

    private fun processHits(caster: LivingEntity, filters: Set<TargetFilter>, hits: List<RaycastHit>): Target {
        val entitiesCollector = CollectionsUtils.TargetCollector<LivingEntity>()
        val blocksCollector = CollectionsUtils.TargetCollector<org.bukkit.block.Block>()
        val eyePos = eyePosition(caster)

        hits.forEach { hit ->
            addHitEntities(hit, filters, entitiesCollector)
            addHitBlocks(hit, filters, blocksCollector)
            if (stopProcessing(hit, filters)) return@forEach
        }

        return Target(entitiesCollector.getSortedByDistance(), blocksCollector.getSortedByDistance(), caster, filters)
    }

    private fun addHitEntities(hit: RaycastHit, filters: Set<TargetFilter>, collector: CollectionsUtils.TargetCollector<LivingEntity>) {
        if (TargetFilter.ENTITIES in filters) hit.hitEntities.forEach { collector.add(it, hit.position.distance(Vec3(0.0,0.0,0.0))) }
    }

    private fun addHitBlocks(hit: RaycastHit, filters: Set<TargetFilter>, collector: CollectionsUtils.TargetCollector<org.bukkit.block.Block>) {
        if (TargetFilter.BLOCKS in filters) hit.hitBlock?.let { collector.add(it, hit.position.distance(Vec3(0.0,0.0,0.0))) }
    }

    private fun stopProcessing(hit: RaycastHit, filters: Set<TargetFilter>): Boolean =
        (TargetFilter.FIRST_ENTITY in filters && hit.hitEntities.isNotEmpty()) ||
                (TargetFilter.STOP_AT_BLOCK in filters && hit.hitBlock != null)
}
