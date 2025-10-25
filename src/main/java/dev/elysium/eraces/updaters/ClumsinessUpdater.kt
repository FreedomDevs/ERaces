package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IUnloadable
import dev.elysium.eraces.updaters.base.IUpdater
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.random.Random

class ClumsinessUpdater : IUpdater, IUnloadable {
    private val plugin = ERaces.getInstance()
    private var taskId: Int = 0
    private val playersWithClumsiness = mutableSetOf<UUID>()

    init {
        startTimer()
    }

    override fun update(race: Race?, player: Player?) {
        if (player == null || race == null) return

        if (race.clumsinessChance.chance > 0.0) {
            playersWithClumsiness.add(player.uniqueId)
        } else {
            playersWithClumsiness.remove(player.uniqueId)
        }
    }

    override fun unload(player: Player?) {
        player?.uniqueId?.let { playersWithClumsiness.remove(it) }
    }

    private fun startTimer() {
        taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            playersWithClumsiness.removeIf { uuid ->
                val player = Bukkit.getPlayer(uuid)
                player == null || !player.isOnline
            }

            for (uuid in playersWithClumsiness) {
                val player = Bukkit.getPlayer(uuid) ?: continue
                val race = plugin.context.playerDataManager.getPlayerRace(player) ?: continue

                if (Random.nextDouble() < race.clumsinessChance.chance) {
                    player.damage(race.clumsinessChance.damage)
                    ChatUtil.message(player, "<orange>Вы оступились и получили урон")
                }
            }
        }, 0L, 20L * 10).taskId
    }
}