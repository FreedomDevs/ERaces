package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.msg
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Halberd(override val plugin: ERaces) : MeleeWeapon(
    id = "halberd",
    material = Material.IRON_SWORD,
    name = "<blue>Бердыш",
    damage = 12.0,
    attackSpeed = 0.8,
    maxDurability = 1300,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>12.0",
            "<gray>⚡ <white>Скорость атаки: <yellow>0.8",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val abilityCooldownMillis = 3000L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT || hand != EquipmentSlot.HAND) return
        val stack = player.inventory.itemInMainHand

        tryAbility(player, stack, abilityCooldownMillis) {
            ability(player)
        }
    }

    private fun ability(player: Player) {
        val chargeTicks = 20L
        val step = 2L
        val totalSteps = chargeTicks / step

        for (i in 0..totalSteps) {
            val world = player.world
            val loc = player.location

            Bukkit.getScheduler().runTaskLater(plugin, Runnable  {
                val remainingTicks = chargeTicks - (i * step)
                val seconds = remainingTicks / 20.0

                player.actionMsg("<yellow>Зарядка: <gold>${"%.1f".format(seconds)}с")

                world.spawnParticle(
                    Particle.ENCHANTED_HIT,
                    loc,
                    5,
                    0.4, 0.2, 0.4,
                    0.0
                )
            }, i * step)
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val world = player.world
            val loc = player.location

            world.spawnParticle(
                Particle.SWEEP_ATTACK,
                loc,
                20,
                0.5, 0.2, 0.5,
                0.0
            )

            world.spawnParticle(
                Particle.CRIT,
                loc,
                50,
                1.5, 0.5, 1.5,
                0.1
            )

            world.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.8f)

            val radius = 4.0

            for (entity in world.getNearbyEntities(loc, radius, radius, radius)) {
                if (entity !is LivingEntity) continue
                if (entity == player) continue

                entity.damage(6.0, player)

                entity.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.BLINDNESS,
                        40,
                        0
                    )
                )
            }

        }, 20L)
    }
}