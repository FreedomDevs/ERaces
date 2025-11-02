package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

class CrossProtectionAbility : BaseCooldownAbility(id = "crossprotection", defaultCooldown = "30s"), Listener {
    private val particleTasks = mutableMapOf<UUID, BukkitTask>()
    private val active = mutableSetOf<UUID>()

    private var duration: String = "1s"

    override fun onActivate(player: Player) {
        val plugin = ERaces.getInstance()

        if (active.contains(player.uniqueId)) {
            player.actionMsg("<red>Ты уже используешь способность 'Перекрестная защита'!")
            return
        }
        active.add(player.uniqueId)

        val task = startParticleTails(player)
        particleTasks[player.uniqueId] = task

        AbilityUtils.runLater(plugin, duration) {
            active.remove(player.uniqueId)
            particleTasks.remove(player.uniqueId)?.cancel()
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player && active.contains(entity.uniqueId)) {
            event.isCancelled = true
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
        }
    }

    private fun startParticleTails(player: Player): BukkitTask {
        val plugin = ERaces.getInstance()
        val world = player.world
        val color = Color.ORANGE
        var angle = 0.0

        return Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (!player.isOnline) return@Runnable

            angle += Math.PI / 8
            val dir = player.location.direction.normalize()

            val front = player.location.add(dir.multiply(0.5))

            val offset1 = Vector(cos(angle) * 0.6, sin(angle * 2) * 0.3, sin(angle) * 0.6)
            val offset2 = Vector(cos(angle + Math.PI) * 0.6, sin(angle * 2 + Math.PI) * 0.3, sin(angle + Math.PI) * 0.6)

            val loc1 = front.clone().add(offset1)
            val loc2 = front.clone().add(offset2)

            world.spawnParticle(
                Particle.DUST,
                loc1,
                2,
                Particle.DustOptions(color, 1.5f)
            )
            world.spawnParticle(
                Particle.DUST,
                loc2,
                2,
                Particle.DustOptions(color, 1.5f)
            )
        }, 0L, 2L)
    }
}