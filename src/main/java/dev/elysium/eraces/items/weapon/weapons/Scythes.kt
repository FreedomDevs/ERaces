package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class Scythes(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "scythes",
    material = Material.IRON_SWORD,
    name = "<red>Серпы",
    damage = 5.0,
    attackSpeed = 2.2,
    isUnbreakable = false,
    maxDurability = 500,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>5.0",
            "<gray>⚡ <white>Скорость атаки: <yellow>2.2",
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

        val weaponState = ItemState(stack)
        val hits = weaponState.addInt(StateKeys.HITS, 1)

        if (hits % 6 == 0) {
            EffectUtils.applyEffects(
                player,
                mapOf("minecraft:speed" to 2),
                TimeParser.parseToTicks("4s").toInt()
            )

            val chance = Random.nextDouble()
            if (chance <= 0.2) {
                event.damage *= 1.5

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
}
