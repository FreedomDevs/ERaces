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

class WhisperingSickles(override val plugin: ERaces) : MeleeWeapon(
    id = "whispering_sickles",
    material = Material.IRON_SWORD,
    name = "dark_purple>Серпы Шепота",
    damage = 6.0,
    attackSpeed = 2.0,
    maxDurability = 650,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>6",
            "<gray>⚡ <white>Скорость атаки: <yellow>2.0",
            "",
            "<gold>✦ <gradient:#b388ff:#7c4dff>Способность: Шептание ягнят</gradient>",
            "<gray>▸ <white>[ПКМ] — <#caa9ff>рывок к цели</#caa9ff>",
            "<gray>▸ <white>4 удара по <red>3</red> урона",
            "<gray>▸ <white>Накладывает <blue>Замедление I</blue>",
            "<gray>▸ <white>Если находится в обоих руках то будет <underlined><bold><red>круче",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val cooldownMillis = 5000L
    private val range = 5.0
    private val hits = 4
    private val hitDamage = 3.0

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

        val isTwoHands = ItemResolver.resolve(player.inventory.itemInOffHand) is WhisperingSickles

        whisperDash(player, target, isTwoHands)
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

    private fun whisperDash(player: Player, target: LivingEntity, isTwoHands: Boolean) {
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

                target.damage(hitDamage + (if (isTwoHands) 2 else 0), player)

                target.world.spawnParticle(
                    Particle.CRIT,
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