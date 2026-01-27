package dev.elysium.eraces.abilities.abils.attack.aoe

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseTargetTrailAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.ignite
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

@RegisterAbility
@Suppress("unused")
class BurnAbility : BaseTargetTrailAbility(
    id = "burn",
    defaultCooldown = "10m",
    comboKey="4608"
) {
    private var duration: String = "10s"

    override fun onHitTarget(player: Player, target: LivingEntity) {
        target.ignite(TimeParser.parseToTicks(duration).toInt())
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("duration", duration)
        }
    }

}