package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.TimeUtil
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

class MurasuSpear : MeleeWeapon(
    id = "murasu_spear",
    material = Material.IRON_SWORD,
    name = "<#FF3399>Копьё Мурасу",
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
    private val hitInterval = 10L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT || hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand

        tryAbilityWithResult(player, stack, cooldownMillis) {
            val result = player.world.rayTraceEntities(
                player.eyeLocation,
                player.eyeLocation.direction,
                totalRange,
                0.1
            ) { entity ->
                entity is LivingEntity && entity != player
            }
            val target = result?.hitEntity as? LivingEntity
            if (target == null) {
                player.actionMsg("<gray>Нет цели в досягаемости…</gray>")
                return@tryAbilityWithResult false
            } else {
                triplePierce(player, target)
                true
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
                            TimeUtil.parseToTicks("3s").toInt(),
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
        }.runTaskTimer(ERaces.getInstance(), 0L, hitInterval)
    }
}