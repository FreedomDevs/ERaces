package dev.elysium.eraces.abilities.abils.special.focus_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.msg
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.UUID

// НЕ РАБОТАЕТ ПОКА ЧТО
class LifeGoingAccordingToPlanAbility : BaseCooldownAbility(
    id = "lifegoingaccordingtoplan",
    defaultCooldown = "5m"
) {
    private var duration = "2m"
    private var radius = 15.0
    private var damage = 15.0

    private var affectedPlayers: MutableMap<UUID, Long>  = mutableMapOf()

    override fun onActivate(player: Player) {
        var isExecuted = false

        Target.Companion.from(player)
            .filter(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY)
            .inEye(radius, 0.1)
            .execute { target -> run {
                    affectedPlayers[target.uniqueId] = System.currentTimeMillis();
                    isExecuted = true
                }
            }

        if (!isExecuted) {
            player.msg("<red>Вы смотрите на игрока, способность не активирована!")
            this.resetCooldown(player)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
            read("damage", ::damage)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
            write("damage", damage)
        }
    }
}