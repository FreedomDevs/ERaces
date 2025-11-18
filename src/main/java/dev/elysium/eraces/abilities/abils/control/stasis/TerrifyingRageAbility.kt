package dev.elysium.eraces.abilities.abils.control.stasis

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.UUID

class TerrifyingRageAbility : BaseCooldownAbility(
    id = "terrifyingrage", defaultCooldown = "90s"
), Listener {
    private var duration: String = "5s"
    private var radiuas: Double = 2.0
    private val activatePlayers = mutableMapOf<UUID, Long>()

    override fun onActivate(player: Player) {
        Target.from(player)
            .inRadius(radiuas)
            .excludeCaster()
            .execute { target ->
                if (target !is Player)
                   return@execute
                activatePlayers[target.player?.uniqueId ?: return@execute] = System.currentTimeMillis()
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radiuas)
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radiuas)
            write("duration", duration)
        }
    }

    @EventHandler
    fun playerMove(event: PlayerMoveEvent){
        val uuid = event.player.uniqueId
        val a = activatePlayers[uuid]?: return
        val now = System.currentTimeMillis()
        val g = now - a
        if (g < TimeParser.parseToMilliseconds(duration)){
           event.isCancelled = true
            return
        }
        else {
            activatePlayers.remove(uuid)
        }
    }
    @EventHandler
    fun playerAttackEvent(event: EntityDamageByEntityEvent){
        if (event.damager !is Player) return
        val uuid = event.damager.uniqueId
        val a = activatePlayers[uuid]?: return
        val now = System.currentTimeMillis()
        val g = now - a
        if (g < TimeParser.parseToMilliseconds(duration)){
            event.isCancelled = true
            return
        }
        else {
            activatePlayers.remove(uuid)
        }
    }
}