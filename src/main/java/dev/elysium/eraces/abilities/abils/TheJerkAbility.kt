package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TheJerkAbility : BaseCooldownAbility(
    id = "thejerk", defaultCooldown = "1m"
) {
    private var jumpDistance: Double = 7.0

    override fun onActivate(player: Player) {
        val direction = player.location.direction.normalize()
        val moveDirection = Vector(direction.x, 0.0, direction.z).normalize()
        val jumpVector = moveDirection.multiply(jumpDistance)
        player.velocity = jumpVector
    }
    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("jumpDistance", ::jumpDistance)

        }
    }
    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("jumpDistance", jumpDistance)
        }
    }
}