package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.ChatUtil
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.safeDamage
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class JerkAbility : BaseCooldownAbility(id = "jerk", defaultCooldown = "5m") {
    private var maxDistance = 15.0
    private var damageLine = 3.0
    private var damageCast = 5.0
    private var damageThrough = 3.0

    override fun onActivate(player: Player) {
        val plugin = ERaces.getInstance()

        player.safeDamage(damageCast)
        player.actionMsg("<blue>Ты совершаешь рывок вперёд!")

        val startLoc = player.location.clone()
        val dir = player.location.direction.normalize()
        var targetLoc = startLoc.clone().add(dir.clone().multiply(maxDistance))

        val target = Target.from(player)
            .filter(TargetFilter.ENTITIES, TargetFilter.STOP_AT_BLOCK)
            .inEye(maxDistance)
            .excludeCaster()

        target.trail(TargetTrail.Config(
            distance = maxDistance,
            step = 0.3,
            particle = Particle.FLAME,
            stopAtBlock = true,
            onHit = { entity ->
                entity.safeDamage(damageThrough, player)
                entity.world.spawnParticle(
                    Particle.CRIT, entity.location.add(0.0, 1.0, 0.0),
                    15, 0.3, 0.3, 0.3, 0.05
                )
            },
            onStep = { pos ->
                player.world.spawnParticle(
                    Particle.DUST,
                    pos.x, pos.y, pos.z,
                    1, 0.0, 0.0, 0.0,
                    Particle.DustOptions(Color.fromRGB(100, 200, 255), 1.5f)
                )
            }
        ))


        val blockHit = target.getBlocks().firstOrNull()
        if (blockHit != null) {
            targetLoc = blockHit.location.clone().subtract(dir.clone().multiply(1.0))
        }

        player.teleport(targetLoc)
        player.world.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f)

        AbilityUtils.runLater(plugin, "5t") {
            player.world.spawnParticle(
                Particle.WITCH, player.location.add(0.0, 1.0, 0.0),
                40, 0.5, 0.5, 0.5, 0.1
            )
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("max_distance", ::maxDistance)
            read("damage_line", ::damageLine)
            read("damage_cast", ::damageCast)
            read("damage_through", ::damageThrough)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("max_distance", maxDistance)
            write("damage_line", damageLine)
            write("damage_cast", damageCast)
            write("damage_through", damageThrough)
        }
    }
}