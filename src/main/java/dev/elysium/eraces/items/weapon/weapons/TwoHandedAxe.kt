package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class TwoHandedAxe(override val plugin: ERaces) : MeleeWeapon(
    id = "two_handed_axe",
    material = Material.IRON_AXE,
    name = "<pink>Двуручный Топор",
    damage = 14.0,
    attackSpeed = 0.9,
    maxDurability = 1500,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>14",
            "<gray>⚡ <white>Скорость атаки: <yellow>0.9",
            "",
            "<gold>✦ <gradient:#b0bec5:#37474f>Способность: Разлом щитов</gradient>",
            "<gray>▸ <white>Удар по активному щиту",
            "<gray>▸ <white>отключает его",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {

    private val shieldCooldownTicks = 100

    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val target = event.entity as? Player ?: return

        if (!target.isBlocking) return

        val offHand = target.inventory.itemInOffHand
        val mainHand = target.inventory.itemInMainHand

        val hasShield =
            offHand.type == Material.SHIELD ||
                    mainHand.type == Material.SHIELD

        if (!hasShield) return

        target.setCooldown(Material.SHIELD, shieldCooldownTicks)
        target.stopSound("item.shield.block")

        target.world.spawnParticle(
            Particle.CRIT,
            target.location.add(0.0, 1.0, 0.0),
            15,
            0.3, 0.3, 0.3,
            0.2
        )
    }
}
