package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class HopSkipDeepAbility : BaseCooldownAbility(
    id = "hopSkipDeep", defaultCooldown = "15s"
) {
    private var jumpDistance: Double = 5.0
    private var jumpHeight: Double = 0.5

    override fun onActivate(player: Player) {
        val direction = player.location.direction.normalize()
        val moveDirection = Vector(direction.x, 0.0, direction.z).normalize()
        val jumpVector = moveDirection.multiply(jumpDistance).setY(jumpHeight)
        player.velocity = jumpVector
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("jumpDistance", ::jumpDistance)
            read("jumpHeight", ::jumpHeight)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("jumpDistance", jumpDistance)
            write("jumpHeight", jumpHeight)
        }
    }
}