package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import net.kyori.adventure.text.Component
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
    model = 1005,
    name = "§cПоврежденный Бердыш",
    damage = 10.0,
    attackSpeed = 0.8,
    maxDurability = 600
) {

    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        if (stack.type.isAir) return

        val weapon = ItemState(stack)
        val hits = weapon.addInt(StateKeys.HITS, 1)

        if (hits % 2 == 0) {
            val newDamage = event.damage * 1.5
            event.damage = newDamage
            player.world.spawnParticle(
                Particle.CRIT,
                event.entity.location.add(0.0, 1.0, 0.0),
                10,
                0.3,
                0.3,
                0.3,
                0.0
            )

            player.world.playSound(
                event.entity.location,
                Sound.ENTITY_PLAYER_ATTACK_CRIT,
                1.0f,
                1.0f
            )
        }
    }
}
