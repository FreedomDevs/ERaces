package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class ShadowJerkAbility: BaseEffectsAbility(
    id = "shadowjerk",
    defaultCooldown = "1m",
    defaultEffects = linkedMapOf(
        "strength" to EffectData("3s", 2, PotionEffectType.STRENGTH),
    )
){
    private var jumpDistance: Double = 3.0

    override fun customActivate(player: Player) {
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

