package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID
import kotlin.collections.mutableMapOf

@RegisterAbility
@Suppress("unused")

class OrkregaliaAbility : BaseCooldownAbility(
    id = "orkregalia", defaultCooldown = "160s"
) {
    private var duration: String = "10s"
    private var activatePlayers = mutableMapOf<UUID, Long>()

    override fun onActivate(player: Player) {
        player.health = player.health / 3

        activatePlayers[player.uniqueId] = System.currentTimeMillis()

    }
    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
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
            event.damage = event.damage * 2.0
            return
        }
        else {
            activatePlayers.remove(uuid)
        }
    }

}