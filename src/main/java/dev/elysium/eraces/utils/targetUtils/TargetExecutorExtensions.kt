package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.target.TargetExecutor
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.Vec3
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity

// ------------------- LivingEntity extensions -------------------

fun LivingEntity.ignite(ticks: Int) = TargetExecutor.ignite(this, ticks)
fun LivingEntity.safeDamage(amount: Double, source: LivingEntity? = null) =
    TargetExecutor.damage(this, amount, source)

fun LivingEntity.slow(duration: Int, amplifier: Int) =
    TargetExecutor.slow(this, duration, amplifier)

fun LivingEntity.knockback(power: Double) = TargetExecutor.knockback(this, power)
fun LivingEntity.safeHeal(amount: Double) = TargetExecutor.heal(this, amount)

// ------------------- TargetFilter extensions -------------------

fun Set<TargetFilter>.includesEntities() = TargetFilter.ENTITIES in this
fun Set<TargetFilter>.stopsAtBlock() = TargetFilter.STOP_AT_BLOCK in this

fun Set<TargetFilter>.shouldCollectEntities() =
    TargetFilter.ENTITIES in this || TargetFilter.FIRST_ENTITY in this

private fun TargetFilter.isEntityFilter() = this == TargetFilter.FIRST_ENTITY
private fun Set<TargetFilter>.shouldStopAtBlock() = TargetFilter.STOP_AT_BLOCK in this

// ------------------- Vec3 -> Location -------------------

fun Vec3.toBukkitLocation(world: World) =
    Location(world, x, y, z)
