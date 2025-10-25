package dev.elysium.eraces.updaters

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class ChanceResurrectionListener : Listener {

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player !is Player) return
        val damage = event.finalDamage
        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(player)

        if (race.chanceResurrection.chance > 0.0) {
            if (player.health - damage <= 0) {
                val key = NamespacedKey(ERaces.getInstance(), "resurrections_done")
                var resurrectionsDone = player.persistentDataContainer.get(key, PersistentDataType.INTEGER) ?: 0

                if (Random.nextDouble() < race.chanceResurrection.chance) {
                    resurrectionsDone++
                    player.persistentDataContainer.set(key, PersistentDataType.INTEGER, resurrectionsDone)

                    val attribute = player.getAttribute(Attribute.MAX_HEALTH) ?: return
                    val newMaxHealth = race.maxHp - race.chanceResurrection.nexHpMinus * resurrectionsDone
                    attribute.baseValue = if (newMaxHealth < 1.0) 1.0 else newMaxHealth
                    player.health = attribute.baseValue

                    ChatUtil.sendAction(player, "Вам удалось избежать объятий смерти")
                } else {
                    resurrectionsDone = 0
                    player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = race.maxHp
                }
            }
        }
    }
}