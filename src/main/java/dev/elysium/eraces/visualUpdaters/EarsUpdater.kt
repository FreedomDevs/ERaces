package dev.elysium.eraces.visualUpdaters

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.UUID

class EarsUpdater : IVisualUpdater {
    private val affectedPlayers: MutableSet<UUID> = mutableSetOf()
    private var task: Int? = null

    val delayInTicks: Long = 18
    val earsTask = Runnable {
        val toRemove = mutableListOf<UUID>()

        for (i: UUID in affectedPlayers) {
            val player = Bukkit.getPlayer(i)
            if (player == null) {
                toRemove.add(i)
                continue
            }

            val loc = player.eyeLocation
            val dir = loc.direction

            val right: Vector = dir.clone().crossProduct(Vector(0, 1, 0)).normalize()

            val leftEar = loc.clone().add(right.clone().multiply(-0.3).add(Vector(0.0, 0.30, 0.0)))
            val rightEar = loc.clone().add(right.clone().multiply(0.3).add(Vector(0.0, 0.30, 0.0)))

            val dustOptions = Particle.DustOptions(Color.fromRGB(255, 200, 0), 1.2f)
            player.spawnParticle(Particle.DUST, leftEar, 5, 0.1, 0.1, 0.1, 0.0, dustOptions)
            player.spawnParticle(Particle.DUST, rightEar, 5, 0.1, 0.1, 0.1, 0.0, dustOptions)
        }

        @Suppress("ConvertArgumentToSet")
        if (toRemove.isNotEmpty()) {
            affectedPlayers.removeAll(toRemove)
            updateTaskRunningState()
        }
    }

    fun updateTaskRunningState() {
        if (affectedPlayers.isNotEmpty() && task == null) {
            ERaces.logger().fine("Affected players not empty, but task is null - starting ears drawer task")
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), earsTask, 0, delayInTicks)
        } else if (affectedPlayers.isEmpty() && task != null) {
            ERaces.logger().fine("Affected players empty, but task is running - cancelling ears drawer task")
            Bukkit.getScheduler().cancelTask(task!!)
            task = null
        }
    }

    override fun updateVisuals(race: Race, player: Player) {
        if (race.visuals.contains("ears")) affectedPlayers.add(player.uniqueId)
        else affectedPlayers.remove(player.uniqueId)
        updateTaskRunningState()
    }

    override fun unloadVisuals(player: Player) {
        affectedPlayers.remove(player.uniqueId)
        updateTaskRunningState()
    }
}
