package dev.elysium.eraces.abilities.core.utils

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Менеджер кулдаунов способностей.
 * Хранит время окончания кулдауна для каждого игрока и способности.
 */
internal object AbilityCooldownManager {

    private val cooldowns: MutableMap<UUID, MutableMap<String, Long>> = ConcurrentHashMap()

    fun hasCooldown(player: Player, abilityId: String): Boolean =
        (cooldowns[player.uniqueId]?.getOrDefault(abilityId, 0L) ?: 0L) > System.currentTimeMillis()

    fun getRemaining(player: Player, abilityId: String): Long {
        val remaining = (cooldowns[player.uniqueId]?.get(abilityId) ?: 0) - System.currentTimeMillis()
        return maxOf(0, TimeUnit.MILLISECONDS.toSeconds(remaining))
    }

    fun setCooldown(player: Player, abilityId: String, seconds: Long) {
        val playerCooldowns = cooldowns.computeIfAbsent(player.uniqueId) { ConcurrentHashMap() }
        playerCooldowns[abilityId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds)
    }

    fun resetCooldown(player: Player, abilityId: String) {
        cooldowns[player.uniqueId]?.remove(abilityId)
    }
}