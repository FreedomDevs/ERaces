package dev.elysium.eraces.abilities.abils.movement.jumps

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

@RegisterAbility
@Suppress("unused")
class TheArboristAbility : BaseCooldownAbility(
    id = "thearborist", defaultCooldown = "15s"
) {
    private var jumpHeight: Double = 5.0

    override fun onActivate(player: Player) {
        player.velocity = player.velocity.setY(jumpHeight)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("jumpHeight", ::jumpHeight)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("jumpHeight", jumpHeight)
        }

    }
}