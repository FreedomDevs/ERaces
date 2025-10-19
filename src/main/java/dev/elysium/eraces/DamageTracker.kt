package dev.elysium.eraces

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.UUID

class DamageTracker(private val damageWindowMillis: Long = 30_000L) : Listener {
    private val damageMap: MutableMap<UUID, MutableMap<UUID, Double>> = HashMap()
    private val lastDeath: MutableMap<UUID, Long> = HashMap()
    private val pairLastKill: MutableMap<Pair<UUID, UUID>, Long> = HashMap()
    private val hourlyXpMap: MutableMap<UUID, Long> = HashMap()

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val victim = e.entity as? Player ?: return
        val attacker = when (e.damager) {
            is Player -> e.damager as Player
            else -> return
        }

        val dmg: Double = e.finalDamage
        if (dmg <= 0.0) return

        val vm = damageMap.computeIfAbsent(victim.uniqueId) { HashMap() }
        vm[attacker.uniqueId] = vm.getOrDefault(attacker.uniqueId, 0.0) + dmg
    }

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val victim = e.entity
        val now = System.currentTimeMillis()
        lastDeath[victim.uniqueId] = now

        damageMap.remove(victim.uniqueId)
    }

    fun getDamageContributions(victimId: UUID, withinMillis: Long = damageWindowMillis): Map<UUID, Double> {
        val contributions = damageMap[victimId] ?: return emptyMap()
        return contributions.toMap()
    }

    fun recordPairKill(killerId: UUID, victimId: UUID) {
        pairLastKill[Pair(killerId, victimId)] = System.currentTimeMillis()
    }

    fun getPairLastKill(killerId: UUID, victimId: UUID): Long {
        return pairLastKill[Pair(killerId, victimId)] ?: 0L
    }

    fun getLastDeath(victimId: UUID): Long {
        return lastDeath[victimId] ?: 0L
    }

    fun addHourlyXp(killerId: UUID, xp: Long) {
        hourlyXpMap[killerId] = hourlyXpMap.getOrDefault(killerId, 0L) + xp
    }

    fun getHourlyXp(killerId: UUID): Long = hourlyXpMap.getOrDefault(killerId, 0L)

    fun cleanupOldEntries(olderThanMillis: Long) {
        val cutoff = System.currentTimeMillis() - olderThanMillis
        pairLastKill.entries.removeIf { it.value < cutoff }
    }
}
