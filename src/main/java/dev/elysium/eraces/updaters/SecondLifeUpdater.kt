package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

class SecondLifeUpdater : Listener {
    private val secondLifeCooldowns: MutableMap<UUID, Long> = mutableMapOf()

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val race: Race = ERaces.getInstance().context.playerDataManager.getPlayerRace(event.player)
        if (race.secondLifeCooldown == "") return

        val player: Player = event.player
        val uuid: UUID = event.player.uniqueId
        val curTime: Long = System.currentTimeMillis()
        val cooldown: Long = TimeParser.parseToMilliseconds(race.secondLifeCooldown)


        if (secondLifeCooldowns.getOrDefault(uuid, 0) + cooldown < curTime) {
            secondLifeCooldowns[uuid] = curTime
            event.isCancelled = true
            player.playSound(player.location, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f)
            player.foodLevel = 20;
            player.health += 5;
            player.addPotionEffects(listOf(
                PotionEffect(
                    PotionEffectType.REGENERATION,
                    100,
                    1,
                    false
                ),
                PotionEffect(
                    PotionEffectType.ABSORPTION,
                    200,
                    1,
                    false
                )
            ))
        }
    }
}