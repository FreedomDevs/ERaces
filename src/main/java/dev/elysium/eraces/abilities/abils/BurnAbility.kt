package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class BurnAbility : BaseCooldownAbility(
    id = "burn",
    defaultCooldown = "10m"
) {
    private var radius: Double = 30.0
    private var duration: String = "10s"

    override fun activate(player: Player) {
        val world = player.world
        val eye = player.eyeLocation.clone()
        val dir = eye.direction.clone().normalize()

        val step = 0.2
        val iterations = (radius / step).toInt()
        val xyiZnaet = eye.clone()
        for (i in 0..iterations) {
            xyiZnaet.add(dir.clone().multiply(step))
            val block = xyiZnaet.block
            if (block.type.isSolid) break
            val nearby = world.getNearbyEntities(xyiZnaet, 0.5, 0.5, 0.5)
            for (ent in nearby) {
                if (ent === player) break
                if (ent is LivingEntity) {
                    ent.fireTicks = TimeParser.parseToTicks(duration).toInt()
                    return
                }
            }
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        radius = cfg.getDouble("radius", radius)
        duration = cfg.getString("duration", duration).toString()
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.set("radius", radius)
        cfg.set("duration", duration)
    }

}
