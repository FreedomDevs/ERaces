package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
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

class BloodSickles(override val plugin: ERaces) : MeleeWeapon(
    id = "blood_sickles",
    material = Material.IRON_SWORD,
    name = "dark_red>Серпы Крови",
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
            "<gray>▸ <white>Если находится в обоих руках то будет <underlined><bold><red>круче",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val cooldownMillis = 5000L
    private val range = 6.0
    private val hits = 6
    private val hitDamage = 2.0

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand
        val state = ItemState(stack)
        val now = System.currentTimeMillis()
        val kdEnd = state.getLong(StateKeys.KD)

        if (now < kdEnd) {
            val remaining = (kdEnd - now) / 1000.0
            player.actionMsg("<red>Способность еще не готова! <gold>${"%.1f".format(remaining)}s</gold>")
            return
        }

        val target = findTarget(player) ?: run {
            player.actionMsg("<gray>Нет цели поблизости…</gray>")
            return
        }

        val isTwoHands = ItemResolver.resolve(player.inventory.itemInOffHand) is BloodSickles

        bloodDash(player, target, isTwoHands)
        state.setLong(StateKeys.KD, now + (cooldownMillis / (if (isTwoHands) 2 else 1)))
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

        player.world.spawnParticle(
            Particle.WHITE_SMOKE,
            player.location.add(0.0, 1.0, 0.0),
            20,
            0.3, 0.3, 0.3,
            0.02
        )

        object : BukkitRunnable() {
            var count = 0

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

                target.world.spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    target.location.clone().add(0.0, target.height * 0.5, 0.0),
                    10,
                    0.2, 0.2, 0.2,
                    0.1
                )

                count++
            }
        }.runTaskTimer(plugin, 0L, 5L)
    }
}
