package dev.elysium.eraces.xpManager.providers

import dev.elysium.eraces.DamageTracker
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.xpManager.PlayerKillXpConfig
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.UUID
import kotlin.math.max

class PlayerKillXpProvider(
    private val config: PlayerKillXpConfig = PlayerKillXpConfig()
) : XpProvider<PlayerDeathEvent> {

    override fun getXp(target: PlayerDeathEvent): Long? {
        val killer = target.entity.killer ?: return null
        val victim: Player = target.entity
        if (killer.uniqueId == victim.uniqueId) return null

        val context = ERaces.getInstance().context
        val specManager = context.specializationsManager
        val damageTracker = context.xpDamageTracker

        if (isOnCooldown(killer.uniqueId, victim.uniqueId, damageTracker)) return null

        val totalVictimXp = getTotalVictimXp(victim, specManager)
        if (totalVictimXp <= 0) return null

        val contributionPercent = getContributionPercent(killer.uniqueId, victim.uniqueId, damageTracker)
        if (contributionPercent < config.minDamagePercentToCount) return null

        var xpToGive = calculateBaseXp(totalVictimXp, contributionPercent)
        xpToGive = applyRecentKillDiminish(xpToGive, killer.uniqueId, victim.uniqueId, damageTracker)
        xpToGive = applyHourlyCap(xpToGive, killer.uniqueId, damageTracker)

        damageTracker.recordPairKill(killer.uniqueId, victim.uniqueId)
        damageTracker.addHourlyXp(killer.uniqueId, xpToGive)

        return xpToGive
    }

    private fun isOnCooldown(killerId: UUID, victimId: UUID, tracker: DamageTracker): Boolean {
        val last = tracker.getPairLastKill(killerId, victimId)
        return System.currentTimeMillis() - last < config.pairCooldownSeconds * 1000
    }

    private fun getTotalVictimXp(victim: Player, specManager: Any): Long {
        val specXp = (specManager as? dev.elysium.eraces.config.SpecializationsManager)
            ?.specPlayersData?.get(victim.uniqueId)?.xp ?: 0L
        return victim.totalExperience.toLong() + specXp
    }

    private fun getContributionPercent(killerId: UUID, victimId: UUID, tracker: DamageTracker): Double {
        val contributions = tracker.getDamageContributions(victimId)
        val totalDamage = contributions.values.sum()
        val killerDamage = contributions[killerId] ?: 0.0
        return if (totalDamage > 0) killerDamage / totalDamage else 0.0
    }

    private fun calculateBaseXp(totalVictimXp: Long, contributionPercent: Double): Long {
        val minBase = (totalVictimXp * config.minPercent).toLong()
        val maxBase = (totalVictimXp * config.maxPercent).toLong()
        val scaledMin = max(1L, (minBase * contributionPercent).toLong())
        val scaledMax = max(scaledMin, (maxBase * contributionPercent).toLong())
        return (scaledMin..scaledMax).random()
    }

    private fun applyRecentKillDiminish(xp: Long, killerId: UUID, victimId: UUID, tracker: DamageTracker): Long {
        val recentKills = estimateRecentPairKills(killerId, victimId, tracker)
        val factor = max(0.2, 1.0 - 0.25 * recentKills)
        return (xp * factor).toLong()
    }

    private fun applyHourlyCap(xp: Long, killerId: UUID, tracker: DamageTracker): Long {
        val alreadyThisHour = tracker.getHourlyXp(killerId)
        if (alreadyThisHour >= config.hourlyXpCap) return 0
        return minOf(xp, config.hourlyXpCap - alreadyThisHour)
    }

    private fun estimateRecentPairKills(killerId: UUID, victimId: UUID, tracker: DamageTracker): Int {
        val last = tracker.getPairLastKill(killerId, victimId)
        if (last == 0L) return 0
        val since = System.currentTimeMillis() - last
        return when {
            since < 10 * 60_000 -> 3
            since < 30 * 60_000 -> 2
            since < 60 * 60_000 -> 1
            else -> 0
        }
    }
}