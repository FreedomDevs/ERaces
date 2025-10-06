package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.ignite
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FireBoomAbility : BaseCooldownAbility(
    id = "fireboom",
    defaultCooldown = "15m"

) {
    private var radius: Double = 3.0
    private var fireDuration: String = "10s"

    override fun activate(player: Player) {
        val world = player.world
        val center = player.location.clone().add(0.0, 0.5, 0.0)
        for (r in 0..radius.toInt()) {
            val circle = 100 * r / radius.toInt()
            for (i in 0..circle) {
                val angle = 2 * PI * i / 100
                val x = cos(angle) * radius
                val z = sin((angle) * radius)

                val location = center.clone().add(x, 0.0, z)
                world.spawnParticle(Particle.FLAME, location, 1)
            }
        }
        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius, true)
            .excludeCaster()
            .execute { it ->
                run {
                    it.ignite(TimeParser.parseToTicks(fireDuration).toInt())
                }
            }


    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        fireDuration = cfg.getString("fireDuration", fireDuration)!!
        radius = cfg.getDouble("radius", radius)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("fireDuration", fireDuration)
        cfg.set("radius", radius)
    }

}