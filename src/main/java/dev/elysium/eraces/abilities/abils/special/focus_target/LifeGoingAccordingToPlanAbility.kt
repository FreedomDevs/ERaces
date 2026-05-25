package dev.elysium.eraces.abilities.abils.special.focus_target

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.msg
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

@RegisterAbility
@Suppress("unused")
class LifeGoingAccordingToPlanAbility : BaseCooldownAbility(
    id = "lifegoingaccordingtoplan",
    defaultCooldown = "5m"
) {
    private var duration = "2m"
    private var radius = 15.0
    private var damage = 15.0

    private val affectedPlayers: MutableMap<UUID, AffectedPlayerData> = mutableMapOf()
    private var task: Int? = null

    private var taskRunnable = Runnable {
        val toRemove: MutableSet<UUID> = mutableSetOf()

        for ((key, value) in affectedPlayers) {
            if (value.startTime + TimeParser.parseToMilliseconds(duration) < System.currentTimeMillis()) {
                toRemove.add(key)
                continue
            }

            val center: Vector = value.centerPos
            val player: Player? = Bukkit.getPlayer(key)
            if (player == null) {
                toRemove.add(key)
                continue
            }

            spawnWarningCube(player.world, player, center, Bukkit.getOnlinePlayers(), 6, 0.7)

            if (!player.location.toVector().isInAABB(
                    center.clone().add(Vector(-radius, Double.NEGATIVE_INFINITY, -radius)),
                    center.clone().add(Vector(radius, Double.POSITIVE_INFINITY, radius))
                )
            ) {
                player.damage(damage)
                toRemove.add(key)
            }
        }

        affectedPlayers.keys.removeAll(toRemove)
    }

    private fun checkTask() {
        if (task == null && affectedPlayers.isNotEmpty()) {
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), taskRunnable, 0, 5)
        } else if (task != null && affectedPlayers.isEmpty()) {
            Bukkit.getScheduler().cancelTask(task!!)
            task = null;
        }
    }

    // Полный GPT код
    fun spawnWarningCube(
        world: World,
        target: Player,
        center: Vector,
        players: Collection<Player>,
        height: Int = 5,
        step: Double = 1.0
    ) {
        val r = this.radius
        var i = 0L

        for (y in 0..height) {
            val yPos = center.y + y
            var x = -r
            while (x <= r) {
                var z = -r
                while (z <= r) {
                    val isEdge = x == -r || x == r || z == -r || z == r
                    if (isEdge) {
                        val px = center.x + x
                        val pz = center.z + z
                        i++


                        // Слабые частицы для всех кроме цели
                        players.filter { it != target }.forEach {
                            if (i % 3 == 0L)
                                it.spawnParticle(Particle.WHITE_SMOKE, px, yPos, pz, 1)
                        }

                        // Яркие красные частицы для цели
                        target.spawnParticle(
                            Particle.DUST, px, yPos, pz, 1,
                            Particle.DustOptions(Color.RED, 1f)
                        )
                    }
                    z += step
                }
                x += step
            }
        }
    }

    override fun onActivate(player: Player) {
        var isExecuted = false

        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inEye(radius, 0.1)
            .execute { target ->
                run {
                    if (target.uniqueId != player.uniqueId) {
                        affectedPlayers[target.uniqueId] =
                            AffectedPlayerData(System.currentTimeMillis(), target.location.toVector())
                        isExecuted = true
                    }
                }
            }

        if (!isExecuted) {
            player.msg("<red>Вы не смотрите на игрока, способность не активирована!")
            this.resetCooldown(player)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
            read("damage", ::damage)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
            write("damage", damage)
        }
    }

    data class AffectedPlayerData(val startTime: Long, val centerPos: Vector)
}