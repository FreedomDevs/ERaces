package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.eParticle.EParticle
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class BloodSickles(override val plugin: ERaces) : MeleeWeapon(
    id = "blood_sickles",
    material = Material.IRON_SWORD,
    name = "<blue>Серпы Крови",
    damage = 4.0,
    attackSpeed = 3.1,
    maxDurability = 650,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>4",
            "<gray>⚡ <white>Скорость атаки: <yellow>3.1",
            "",
            "<gold>✦ <gradient:#ff4d4d:#8b0000>Способность: Кровавая тишина</gradient>",
            "<gray>▸ <white>[ПКМ] — <#ff5555>рывок к цели</#ff5555>",
            "<gray>▸ <white>6 ударов по <red>2</red> урона",
            "<gray>▸ <white>Если находится в обоих руках то будет <red>круче",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val cooldownMillis = 5000L
    private val range = 10.0
    private val hits = 6
    private val hitDamage = 2.0

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand
        tryAbility(player, stack, cooldownMillis) {
            val target = findTarget(player) ?: run {
                player.actionMsg("<gray>Нет цели поблизости…</gray>")
                return@tryAbility
            }

            val isTwoHands = ItemResolver.resolve(player.inventory.itemInOffHand) is BloodSickles
            bloodDash(player, target, isTwoHands)
        }
    }


    private fun findTarget(player: Player): LivingEntity? {
        val result = player.world.rayTraceEntities(
            player.eyeLocation,
            player.eyeLocation.direction,
            range
        ) { entity ->
            entity is LivingEntity && entity != player
        }

        return result?.hitEntity as? LivingEntity
    }

    private fun bloodDash(player: Player, target: LivingEntity, isTwoHands: Boolean) {
        val direction = target.location.toVector().subtract(player.location.toVector()).normalize()

        player.velocity = direction.multiply(1.8)

        EParticle.cloud(player.world, player.location.add(0.0, 1.0, 0.0), count = 20, speed = 0.02)

        object : BukkitRunnable() {
            var count = 1

            override fun run() {
                if (count >= hits || target.isDead) {
                    cancel()

                    player.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.SLOWNESS,
                            TimeParser.parseToTicks("3s").toInt(),
                            0
                        )
                    )
                    return
                }

                target.damage(hitDamage + (if (isTwoHands) 4 else 0), player)

                EParticle.damageIndicator(
                    target.world,
                    target.location.clone().add(0.0, target.height * 0.5, 0.0)
                )

                count++
            }
        }.runTaskTimer(plugin, 0L, 10L)
    }
}
