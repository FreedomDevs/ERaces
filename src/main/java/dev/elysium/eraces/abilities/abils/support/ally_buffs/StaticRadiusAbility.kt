package dev.elysium.eraces.abilities.abils.support.ally_buffs

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.listeners.custom.ManaRegenerationEvent
import dev.elysium.eraces.utils.TimeValue
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

@RegisterAbility
@Suppress("unused")
class StaticRadiusAbility : BaseCooldownAbility(id = "static_radius", defaultCooldown = "50s"), Listener {
    private var radius: Double = 4.0
    private var duration = TimeValue.fromSeconds(30)

    private val boostedPlayers: MutableSet<UUID> = mutableSetOf()

    override fun onActivate(player: Player) {
        val plugin = ERaces.getInstance()
        val nearPlayers = player.getNearbyEntities(radius, radius, radius).filterIsInstance<Player>().toMutableList()

        nearPlayers.add(player)
        nearPlayers.forEach { boostedPlayers.add(it.uniqueId) }
        AbilityUtils.runLater(plugin, duration.toTicks()) {
            nearPlayers.forEach { boostedPlayers.remove(it.uniqueId) }
        }

    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("radius", ::radius)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("radius", radius)
        }
    }

    @EventHandler
    fun onManaRegen(event: ManaRegenerationEvent) {
        val player = event.player
        if (boostedPlayers.contains(player.uniqueId))  {
            event.regenAmount *= 2
        }
    }
}