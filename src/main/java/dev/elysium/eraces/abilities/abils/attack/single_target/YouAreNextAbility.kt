package dev.elysium.eraces.abilities.abils.attack.single_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.msg
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
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
        var isExecuted = false

        Target.from(player)
            .filter(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY)
            .inEye(radius, 0.2)
            .execute { target ->
                run {
                    val loc = target.location.clone()
                    val direction = target.eyeLocation.direction.normalize()
                    loc.add(direction.multiply(-1.5))
                    loc.y = target.location.y

                    player.teleport(loc)

                    player.location.world.strikeLightningEffect(player.location)
                    player.damage(selfDamage, player)
                    isExecuted = true
                }
            }

        if (!isExecuted) {
            player.msg("<red>Вы не смотрите на игрока, способность не активирована!")
            this.resetCooldown(player)
        }
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