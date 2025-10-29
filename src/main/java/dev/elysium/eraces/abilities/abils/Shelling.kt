package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import kotlin.random.Random

class Shelling : BaseCooldownAbility ( id = "shelling", defaultCooldown = "10m") {
    private var radius: Double = 5.0
    private var duration: String = "10s"
    private var arrowFallsEveryTick: Long = 10

    override fun onActivate(player: Player) {
        val timeEnd: Long = System.currentTimeMillis() + TimeParser.parseToMilliseconds(duration)
        var iterationsCount: Long = 0

        var task: BukkitTask? = null
        task = Bukkit.getScheduler().runTaskTimer(ERaces.getInstance(), Runnable {
            if (!player.isOnline && timeEnd < System.currentTimeMillis()) {
                task!!.cancel()
                return@Runnable
            }

            val x = player.location.x
            val y = player.location.y
            val z = player.location.z

            val location = Location(player.world, Random.nextDouble(x - radius, x + radius), y + 9, Random.nextDouble( z - radius, z + radius), 0f, 90f)

            val effects = listOf(
                PotionType.SLOWNESS,
                PotionType.HARMING,
                PotionType.POISON,
                PotionType.WEAKNESS,
            )

            val arrow: Arrow = location.world.spawn(location, Arrow::class.java)
            arrow.shooter = player
            arrow.velocity = Vector(0, -1, 0).multiply(2)
            arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            arrow.basePotionType = effects.random()

            iterationsCount++
        }, 0, arrowFallsEveryTick)

    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("duration", duration)
        }
    }
}