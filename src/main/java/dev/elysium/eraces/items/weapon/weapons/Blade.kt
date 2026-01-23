package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.msg
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot

class Blade(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "blade",
    material = Material.IRON_SWORD,
    model = 1007,
    name = "<red>Клинок",
    damage = 9.0,
    attackSpeed = 1.6,
    maxDurability = 950,
    options = mapOf(
        "lore" to listOf(
            "<dark_gray>──────────────",
            "<gray>⚔ <white>Урон: <red>9",
            "<gray>⚡ <white>Скорость атаки: <yellow>1.6",
            "",
            "<gold>✦ <gradient:#ffcc66:#ff9966>Способность: Выпад</gradient>",
            "<gray>▸ <white>[ПКМ] — <#ffb347>рывок вперёд</#ffb347>",
            "<gray>▸ <white>Наносит <red>5</red> <gray>урона всем на пути",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
            "<dark_gray>──────────────"
        )
    )
) {Ywe

    private val abilityCooldownMillis = 3000L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return
        val stack = player.inventory.itemInMainHand
        val state = ItemState(stack)
        val now = System.currentTimeMillis()
        val kdEnd = state.getLong(StateKeys.KD)
        if (now < kdEnd) {
            val remaining = ((kdEnd - now) / 1000.0)
            player.msg("<red>Способность еще не готова! <gold>${"%.1f".format(remaining)}s</gold>")
            return
        }
        dash(player)
        state.setLong(StateKeys.KD, now + abilityCooldownMillis)
    }

    private fun dash(player: Player) {
        val start = player.eyeLocation.clone()
        val direction = start.direction.normalize()
        val world = player.world

        val maxDistance = 3.0
        val hitRadius = 0.7
        val damage = 5.0

        val step = 0.2
        var traveled = 0.0

        while (traveled <= maxDistance) {
            val point = start.clone().add(direction.clone().multiply(traveled))

            world.spawnParticle(
                Particle.SWEEP_ATTACK,
                point,
                1,
                0.0, 0.0, 0.0,
                0.0
            )

            world.spawnParticle(
                Particle.CLOUD,
                point,
                1,
                0.0, 0.0, 0.0,
                0.01
            )

            traveled += step
        }

        player.velocity = direction.multiply(1.5)

        val result = world.rayTraceEntities(
            start,
            direction,
            maxDistance,
            hitRadius
        ) { entity ->
            entity is LivingEntity && entity != player
        }

        val hit = result?.hitEntity as? LivingEntity ?: return

        hit.damage(damage, player)

        world.spawnParticle(
            Particle.CRIT,
            hit.location.clone().add(0.0, hit.height * 0.5, 0.0),
            15,
            0.3, 0.3, 0.3,
            0.2
        )
    }
}

