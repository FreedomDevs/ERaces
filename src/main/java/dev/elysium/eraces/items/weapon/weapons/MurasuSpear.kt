package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class MurasuSpear(override val plugin: ERaces) : MeleeWeapon(
    id = "murasu_spear",
    material = Material.TRIDENT,
    name = "<pink>Копьё Мурасу",
    damage = 11.0,
    attackSpeed = 1.0,
    maxDurability = 1349,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>11",
            "<gray>⚡ <white>Скорость атаки: <yellow>1.0",
            "",
            "<aqua>✦ Пассивное действие:</aqua>",
            "<gray>▸ <white>+2 к досягаемости",
            "",
            "<gold>✦ <gradient:#4fc3f7:#01579b>Способность: Тройной прокол</gradient>",
            "<gray>▸ <white>3 удара по <red>5</red> урона",
            "<gray>▸ <white>Стан <red>3 сек</red>",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val baseRange = 4.5
    private val bonusRange = 2.0
    private val totalRange = baseRange + bonusRange

    private val cooldownMillis = 7000L
    private val hitDamage = 5.0
    private val hits = 3
    private val hitInterval = 6L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT || hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand

        tryAbility(player, stack, cooldownMillis) {
            val target = findLivingTarget(player, totalRange)
            if (target == null) {
                player.actionMsg("<gray>Нет цели в досягаемости…</gray>")
            } else {
                triplePierce(player, target)
            }
        }
    }


    private fun triplePierce(player: Player, target: LivingEntity) {
        object : BukkitRunnable() {
            var count = 0

            override fun run() {
                if (count >= hits || target.isDead) {
                    cancel()

                    target.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.SLOWNESS,
                            TimeParser.parseToTicks("3s").toInt(),
                            100,
                            false,
                            false,
                            true
                        )
                    )
                    return
                }

                target.damage(hitDamage, player)

                target.world.spawnParticle(
                    Particle.CRIT,
                    target.location.add(0.0, target.height * 0.6, 0.0),
                    12,
                    0.2, 0.2, 0.2,
                    0.2
                )

                count++
            }
        }.runTaskTimer(plugin, 0L, hitInterval)
    }
}