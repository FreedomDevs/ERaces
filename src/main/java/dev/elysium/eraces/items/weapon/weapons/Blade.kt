package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.eParticle.EParticle
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
    name = "<blue>Клинок",
    damage = 8.0,
    attackSpeed = 1.6,
    maxDurability = 950,
    options = mapOf(
        "lore" to listOf(
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
        )
    )
) {

    private val abilityCooldownMillis = 3000L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT || hand != EquipmentSlot.HAND) return
        val stack = player.inventory.itemInMainHand

        tryAbility(player, stack, abilityCooldownMillis, onActivate = {
            dash(player)
        })
    }

    private fun dash(player: Player) {
        val start = player.eyeLocation.clone()
        val direction = start.direction.normalize()
        val world = player.world

        val maxDistance = 3.0
        val hitRadius = 0.7
        val damage = 5.0

        val step = 0.2

        EParticle.lineEffect(world, start, direction, maxDistance, step = step, particle = Particle.SWEEP_ATTACK)
        EParticle.lineEffect(world, start, direction, maxDistance, step = step, particle = Particle.CLOUD)

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

        EParticle.crit(world, hit, 15)
    }
}

