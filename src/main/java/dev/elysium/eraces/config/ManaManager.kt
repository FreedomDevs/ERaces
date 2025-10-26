package dev.elysium.eraces.config

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.SpecializationPlayerData
import dev.elysium.eraces.listeners.custom.ManaConsumeEvent
import dev.elysium.eraces.listeners.custom.ManaRegenerationEvent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

class ManaManager(private val plugin: ERaces) {
    private val manaMap = ConcurrentHashMap<UUID, Double>()
    private val regenIntervalTicks = 60L
    private val regenTaskId: Int

    init {
        regenTaskId = Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                plugin,
                Runnable { restoreAllPlayersMana() },
                regenIntervalTicks,
                regenIntervalTicks
            ).taskId
    }

    fun useMana(player: Player, amount: Double): Boolean {
        ensurePlayerInitialized(player)

        val current = getMana(player)
        val event = ManaConsumeEvent(player, amount)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled()) return false

        val finalAmount = event.amount
        if (current < finalAmount) return false

        manaMap[player.uniqueId] = current - finalAmount
        return true
    }

    fun getMana(player: OfflinePlayer): Double {
        ensurePlayerInitialized(player)
        return manaMap[player.uniqueId] ?: 0.0
    }

    fun getMaxMana(player: OfflinePlayer): Double {
        val data = plugin.context.specializationsManager.specPlayersData[player.uniqueId]
        return data?.INT ?: 0.0
    }

    fun setMana(player: OfflinePlayer, amount: Double) {
        val max = getMaxMana(player)
        manaMap[player.uniqueId] = min(amount, max)
    }

    fun recalcMana(player: OfflinePlayer) {
        val max = getMaxMana(player)
        val current = getMana(player)
        manaMap[player.uniqueId] = min(current, max)
    }

    private fun restoreAllPlayersMana() {
        for (uuid in manaMap.keys) {
            val playerData = plugin.context.specializationsManager.specPlayersData[uuid] ?: continue
            val player = Bukkit.getOfflinePlayer(uuid)
            restorePlayerMana(player, playerData)
        }
    }

    private fun restorePlayerMana(player: OfflinePlayer, data: SpecializationPlayerData) {
        val maxMana = data.INT
        val currentMana = getMana(player)

        if (currentMana >= maxMana) return
        var regenAmount = 1.0 + ((maxMana - currentMana) / maxMana) * 2.0

        val event = ManaRegenerationEvent(player, regenAmount)
        Bukkit.getPluginManager().callEvent(event)

        if (event.isCancelled()) return

        regenAmount = event.regenAmount
        setMana(player, currentMana + regenAmount)
    }

    private fun ensurePlayerInitialized(player: OfflinePlayer) {
        if (manaMap.contains(player.uniqueId)) return
        manaMap.putIfAbsent(player.uniqueId, getMaxMana(player))
    }

    fun disable() {
        Bukkit.getScheduler().cancelTask(regenTaskId)
    }

    fun getMana(player: Player) = getMana(player as OfflinePlayer)
    fun getMaxMana(player: Player) = getMaxMana(player as OfflinePlayer)
    fun setMana(player: Player, amount: Double) = setMana(player as OfflinePlayer, amount)
    fun recalcMana(player: Player) = recalcMana(player as OfflinePlayer)
}