package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.listeners.custom.ManaConsumeEvent
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.UUID

class ShamanRadiusAbility: BaseCooldownAbility(id = "shaman_radius", defaultCooldown = "60s"), Listener {
    private var radius: Double = 4.0
    private var duration: String = "30s"

    private val boostedPlayers: MutableSet<UUID> = mutableSetOf()

    override fun onActivate(player: Player) {
        val plugin = ERaces.getInstance()
        val nearPlayers = player.getNearbyEntities(radius, radius, radius).filterIsInstance<Player>().toMutableList()

        nearPlayers.add(player)
        nearPlayers.forEach { boostedPlayers.add(it.uniqueId) }
        AbilityUtils.runLater(plugin, duration) {
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
    fun onManaConsume(event: ManaConsumeEvent) {
        val player = event.player
        if (boostedPlayers.contains(player.uniqueId))  {
            event.amount /= 2
        }
    }
}