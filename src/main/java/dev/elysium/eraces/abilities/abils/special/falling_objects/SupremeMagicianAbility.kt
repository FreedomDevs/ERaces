package dev.elysium.eraces.abilities.abils.special.falling_objects

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random

@RegisterAbility
@Suppress("unused")
class SupremeMagicianAbility : BaseCooldownAbility( id = "supreme_magician", defaultCooldown = "25m") {
    private var radius: Double = 5.0
    private var step: Double = 0.25
    private var duration: String = "10s"
    private var sphereFallsEveryTick: Long = 10

    private var fireSphereDamage: Double = 4.0
    private var lightBlueSphereDamage: Double = 4.0
    private var iceSphereDamage: Double = 4.0

    private var iceSphereSlownessLevel: Int = 2
    private var iceSphereSlownessDuration: String = "3s"

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

            val location = Location(
                player.world,
                Random.Default.nextDouble(x - radius, x + radius),
                y + 7,
                Random.Default.nextDouble(z - radius, z + radius),
                0f,
                90f
            )

            val spheres = listOf(
                FallingSphereConfig(Particle.FLAME, { entity -> run {
                    entity.damage(fireSphereDamage, player)
                    entity.fireTicks += 20
                }}),
                FallingSphereConfig(Particle.SOUL_FIRE_FLAME, { entity -> run {
                    entity.damage(lightBlueSphereDamage, player)
                }}),
                FallingSphereConfig(Particle.ITEM_SNOWBALL, { entity -> run {
                    entity.damage(iceSphereDamage, player)
                    entity.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.SLOWNESS,
                            TimeParser.parseToTicks(iceSphereSlownessDuration).toInt(),
                            iceSphereSlownessLevel - 1,
                            false
                        )
                    )
                }})
            )

            val selectedSphere: FallingSphereConfig = spheres.random()

            Target.Companion.from(location)
                .filter(*arrayOf(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY))
                .trail(
                    TargetTrail.Config(
                    distance = 20.0,
                    step = step,
                    particle = selectedSphere.particle,
                    stopAtBlock = true,
                    onStep = { loc -> },
                    onHit = selectedSphere.onHit
                ))

            iterationsCount++
        }, 0, sphereFallsEveryTick)

    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("step", ::step)
            read("duration", ::duration)
            read("sphere_falls_every_tick", ::sphereFallsEveryTick)
            read("fire_sphere_damage", ::fireSphereDamage)
            read("light_blue_sphere_damage", ::lightBlueSphereDamage)
            read("ice_sphere_damage", ::iceSphereDamage)
            read("ice_sphere_slowness_level", ::iceSphereSlownessLevel)
            read("ice_sphere_slowness_duration", ::iceSphereSlownessDuration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("step", step)
            write("duration", duration)
            write("sphere_falls_every_tick", sphereFallsEveryTick)
            write("fire_sphere_damage", fireSphereDamage)
            write("light_blue_sphere_damage", lightBlueSphereDamage)
            write("ice_sphere_damage", iceSphereDamage)
            write("ice_sphere_slowness_level", iceSphereSlownessLevel)
            write("ice_sphere_slowness_duration", iceSphereSlownessDuration)
        }
    }

    data class FallingSphereConfig(val particle: Particle, val onHit: ((LivingEntity) -> Unit))
}