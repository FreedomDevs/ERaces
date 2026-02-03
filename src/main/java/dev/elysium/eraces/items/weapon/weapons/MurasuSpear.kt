package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.eParticle.EParticle
import dev.elysium.eraces.utils.targetUtils.safeDamage
import org.bukkit.Material
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
        "lore" to WeaponLoreBuilder.build(
            damage = 11.0,
            attackSpeed = 1.0,
            passiveEffects = listOf(
                "Пассивное действие:" to listOf(
                    "<gray>▸ <white>+2 к досягаемости"
                )
            ),
            activeAbilities = listOf(
                "<gradient:#4fc3f7:#01579b>Способность: Тройной прокол</gradient>" to listOf(
                    "<gray>▸ <white>3 удара по <red>5</red> урона",
                    "<gray>▸ <white>Стан <red>3 сек</red>"
                )
            )
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

                target.safeDamage(hitDamage, player)

                EParticle.crit(target.world, target)

                count++
            }
        }.runTaskTimer(plugin, 0L, hitInterval)
    }
}