package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.bukkit.configuration.file.YamlConfiguration

class FireballAbility : BaseCooldownAbility(
    id = "fireball",
    defaultCooldown = "10m"
) {
    private var power: Double = 2.0
    private val speed: Double = 0.5
    private val durationTicks = 40L

    override fun activate(player: Player) {
        val startLocation = player.eyeLocation.clone()
        val direction: Vector = startLocation.direction.normalize()

        object : BukkitRunnable() {
            var step = 0
            override fun run() {
                if (step > durationTicks) {
                    cancel()
                    return
                }

                val currentLocation = startLocation.clone().add(direction.clone().multiply(step * speed))

                player.world.spawnParticle(
                    Particle.FLAME,
                    currentLocation,
                    2, 0.1, 0.1, 0.1, 0.0
                )

                val hit = currentLocation.world.getNearbyEntities(
                    currentLocation,
                    0.5, 0.5, 0.5
                ).filterIsInstance<Player>()
                    .firstOrNull { it != player }

                if (hit != null) {
                    hit.damage(power, player)
                    hit.fireTicks = 60
                    player.sendMessage("§cТы попал по игроку §e${hit.name}!")
                    cancel()
                    return
                }

                step++
            }
        }.runTaskTimer(ERaces.getInstance(), 0L, 1L)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        power = cfg.getDouble("power", 2.0)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("power", 2.0)
    }
}