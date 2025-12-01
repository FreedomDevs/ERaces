package dev.elysium.eraces.abilities.abils.attack.single_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class YouAreNextAbility : BaseEffectsAbility(
    id = "youarenext",
    defaultCooldown = "2m",
    defaultEffects = linkedMapOf(
        "strength" to EffectData("4s", 3, PotionEffectType.STRENGTH),
    )
) {
    private var selfDamage = 9.0
    private var radius = 40.0

    override fun customActivate(player: Player) {

    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("self_damage", ::selfDamage)
            read("radius", ::radius)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("self_damage", selfDamage)
            write("radius", radius)
        }
    }
}