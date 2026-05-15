package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class SplitChakrams : MeleeWeapon(
    id = "split_chakrams",
    material = Material.IRON_SWORD,
    name = "<#FF3399>Разделённые Чакрам",
    damage = 5.0,
    attackSpeed = 3.3,
    maxDurability = 750,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 5.0,
            attackSpeed = 3.3,
            activeAbilities = listOf(
                "<gradient:#66ccff:#3366ff>Способность: Блок</gradient>" to listOf(
                    "<gray>▸ <white>Блокирует <aqua>40%</aqua> получаемого урона"
                )
            )
        )
    )
), Listener {
    private val cooldownMillis = 10000L
    private val durationMillis = 5000L
    private val blockedPlayers: MutableMap<UUID, Long> = HashMap()

    fun sendActionBar(player: Player, estimatedTime: Long) {
        player.actionMsg(
            "Вы под блоком, блок закончится через %time% секунд",
            Pair("%time%", String.format("%.2f", estimatedTime.toDouble() / 1000))
        )
    }

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand
        tryAbility(player, stack, cooldownMillis) {
            blockedPlayers[player.uniqueId] = System.currentTimeMillis()
            sendActionBar(player, durationMillis)
        }
    }

    @EventHandler
    private fun onDamageEvent(event: EntityDamageEvent) {
        val target = event.entity as? Player ?: return
        val startedTime = blockedPlayers[target.uniqueId] ?: return

        val estimatedTime: Long = durationMillis - (System.currentTimeMillis() - startedTime)
        if (estimatedTime <= 0) {
            blockedPlayers.remove(target.uniqueId)
            return
        }

        sendActionBar(target, estimatedTime)
        event.damage *= 0.6
    }
}