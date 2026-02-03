package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.eParticle.EParticle
import dev.elysium.eraces.utils.eParticle.ParticleConfig
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
    name = "<blue>Поврежденный Бердыш",
    damage = 10.0,
    attackSpeed = 0.8,
    maxDurability = 600,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 10.0,
            attackSpeed = 0.8
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

            EParticle.spawn(player.world, victimLoc, ParticleConfig.Builder(Particle.CRIT)
                .count(30)
                .offset(0.5, 0.7, 0.5)
                .extra(0.2)
                .build()
            )

            EParticle.spawn(player.world, victimLoc, ParticleConfig.Builder(Particle.END_ROD)
                .count(15)
                .offset(0.3, 0.5, 0.3)
                .extra(0.1)
                .build()
            )

            EParticle.spawn(player.world, victimLoc, ParticleConfig.Builder(Particle.WITCH)
                .count(10)
                .offset(0.2, 0.5, 0.2)
                .extra(0.05)
                .build()
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
