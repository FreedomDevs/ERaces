package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamagedHalberd(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "damaged_halberd",
    material = Material.IRON_AXE,
    name = "<red>Поврежденный Бердыш",
    damage = 10.0,
    attackSpeed = 0.8,
    maxDurability = 600,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>10.0",
            "<gray>⚡ <white>Скорость атаки: <yellow>0.8",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {

    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        if (stack.type.isAir) return

        val hits = ItemState(stack).getInt(StateKeys.HITS)

        if (hits % 2 == 0) {
            event.damage *= 1.5

            val victimLoc = event.entity.location.clone().add(0.0, 1.0, 0.0)

            event.entity.world.spawnParticle(
                Particle.CRIT,
                victimLoc,
                30,
                0.5, 0.7, 0.5,
                0.2
            )

            event.entity.world.spawnParticle(
                Particle.END_ROD,
                victimLoc,
                15,
                0.3, 0.5, 0.3,
                0.1
            )

            event.entity.world.spawnParticle(
                Particle.WITCH,
                victimLoc,
                10,
                0.2, 0.5, 0.2,
                0.05
            )

            event.entity.world.playSound(
                victimLoc,
                Sound.ENTITY_PLAYER_ATTACK_CRIT,
                1.0f,
                1.0f
            )
        }

    }
}
