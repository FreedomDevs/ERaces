package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.ignite
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TheMagicBarrierAbility : BaseCooldownAbility(
    id = "themagicbarrier", defaultCooldown = "8m"

) {
    private var radius: Double = 5.0

    private var duration: String = "15s"
    private var level: Int = 3

    override fun activate(player: Player) {
        val world = player.world
        val center = player.location.clone().add(0.0, 0.5, 0.0)
        for (r in 0..radius.toInt()) {
            val circle = 100 * r / radius.toInt()
            for (i in 0..circle) {
                val angle = 2 * PI * i / 100
                val x = cos(angle) * radius
                val z = sin((angle) * radius)
                val option = Particle.DustOptions(Color.YELLOW, 1.5f)
                val location = center.clone().add(x, 0.0, z)
                world.spawnParticle(Particle.DUST, location, 1, option)

            }
        }
        Target.from(player).filter(TargetFilter.ENTITIES).inRadius(radius).execute { it ->
                run {
                    if (it is Player) {
                        val effect = PotionEffect(
                            PotionEffectType.ABSORPTION, TimeParser.parseToTicks(duration).toInt(), level, false, false
                        )
                        it.player?.addPotionEffect(effect)
                    }
                }

            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        duration = cfg.getString("duration", duration).toString()
        level = cfg.getInt("level", level)
        radius = cfg.getDouble("radius", radius)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("duration", duration)
        cfg.set("level", level)
        cfg.set("radius", radius)
    }
}