package dev.elysium.eraces.abilities.abils.attack.magic_damage

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseTargetTrailAbility
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.random.Random

class FireballAbility : BaseTargetTrailAbility(
    id = "fireball",
    defaultCooldown = "10m"
), IManaCostAbility {

    private var power: Double = 2.0
    private var burnDuration: String = "3s"
    private var manaCost: Double = 3.0
    override var distance: Double = 10.0
    override var step: Double = 0.5
    override var particle: Particle = Particle.FLAME
    override var useBaseParticle: Boolean = false

    override val customOnStep: (Location) -> Unit = let@{ loc ->
        val world = loc.world ?: return@let

        spawnParticle(world, Particle.FLAME, loc, count = 10, offset = 0.25, speed = 0.01)
        spawnParticle(world, Particle.SMOKE, loc.clone().subtract(loc.direction.multiply(0.3)), count = 5, offset = 0.15, speed = 0.01)
        spawnParticle(world, Particle.CRIT, loc, count = 2, offset = 0.1, speed = 0.01)
        spawnParticle(world, Particle.FLAME, loc, count = Random.Default.nextInt(8, 15), offset = 0.25, speed = 0.01)

        if (Random.Default.nextDouble() < 0.1) {
            world.playSound(loc, Sound.ITEM_FIRECHARGE_USE, 0.5f, 1.2f)
        }
    }

    override fun onHitTarget(player: Player, target: LivingEntity) {
        target.damage(power, player)
        target.fireTicks = TimeParser.parseToTicks(burnDuration).toInt()
        player.sendMessage("§cТы попал по игроку §e${target.name}!")
    }

    override fun getFilters(): Array<TargetFilter> =
        arrayOf(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY)

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("power", ::power)
            read("burnDuration", ::burnDuration)
            read("distance", ::distance)
            read("step", ::step)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("power", power)
            write("burnDuration", burnDuration)
            write("distance", distance)
            write("step", step)
        }
    }

    private fun spawnParticle(
        world: World,
        particle: Particle,
        location: Location,
        count: Int,
        offset: Double,
        speed: Double
    ) {
        world.spawnParticle(particle, location, count, offset, offset, offset, speed)
    }

    override fun getManaCost() = manaCost
}