package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.titleMsg
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.random.Random

class TwoHandedAxe : MeleeWeapon(
    id = "two_handed_axe",
    material = Material.IRON_AXE,
    name = "<#FF3399>Двуручный Топор",
    damage = 14.0,
    attackSpeed = 0.9,
    maxDurability = 1500,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 14.0,
            attackSpeed = 0.9,
            activeAbilities = listOf(
                "<gradient:#b0bec5:#37474f>Способность: Казнь</gradient>" to listOf(
                    "<gray>▸ <white>Если у цели осталось меньше 4-х хп,",
                    "<gray>▸ <white>цель умирает через 3 секунды, кулдаун - 20 секунд"
                )
            )
        )
    )
) {
    private val cooldown = 20000L
    private val playersForExecution: MutableMap<UUID, MutableSet<UUID>> = HashMap()

    fun title(player: Player) {
        val text = when (Random.nextInt(1, 5)) {
            1 -> "<red>Вы умрёте"
            2 -> "<red>Вы здохните в муках"
            3 -> "<red>Вам пизда"
            4 -> "<red>Гори в аду"
            else -> "Что-то сломалось"
        }

        player.titleMsg(text)
    }

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val result =
            player.world.rayTraceEntities(player.eyeLocation, player.eyeLocation.direction, 6.0, 0.2) { entity ->
                entity is Player && entity != player
            }
        val target = result?.hitEntity as? Player ?: return

        val uuid = player.uniqueId
        val stack = player.inventory.itemInMainHand

        val health = target.health
        if (health <= 0 || health >= 4) return

        if (playersForExecution[uuid] != null) {
            if (!(playersForExecution[uuid]!!.contains(target.uniqueId))) {
                title(target)
                playersForExecution[uuid]!!.add(target.uniqueId)
            }
            return
        }

        tryAbility(player, stack, cooldown) {
            title(target)
            playersForExecution[uuid] = setOf(target.uniqueId) as MutableSet<UUID>

            object : BukkitRunnable() {
                override fun run() {
                    val playerset = playersForExecution[uuid] ?: return

                    for (i in playerset) {
                        val playertokill = Bukkit.getPlayer(i) ?: return

                        playertokill.damage(Double.MAX_VALUE, player)
                    }

                    playersForExecution.remove(uuid)
                }
            }.runTaskLater(ERaces.getInstance(), 20L * 3)
        }
    }
}
