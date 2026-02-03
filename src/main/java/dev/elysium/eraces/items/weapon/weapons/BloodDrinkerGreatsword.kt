package dev.elysium.eraces.items.weapon.weapons

import org.bukkit.attribute.Attribute
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.targetUtils.safeHeal
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class BloodDrinkerGreatsword(override val plugin: ERaces) : MeleeWeapon(
    id = "blooddrinker_greatsword",
    material = Material.IRON_SWORD,
    name = "<pink>Двуручный Меч Кровопийца",
    damage = 10.0,
    attackSpeed = 1.7,
    maxDurability = 1120,
    options = mapOf(
        "lore" to listOf(
            "<gray>⚔ <white>Урон: <red>10",
            "<gray>⚡ <white>Скорость атаки: <yellow>1.7",
            "",
            "<gold>✦ <gradient:#ff3b3b:#7a0000>Пассивное действие: Поглотитель кровей</gradient>",
            "<gray>▸ <white>10% шанс восстановить",
            "<gray>▸ <red>50%</red> от нанесённого урона",
            "",
            "<gray>⛏ <white>Прочность:",
            "<gray>[ <white>{current_durability}<gray> / <white>{durability} <gray>]",
            "{bar}",
        )
    )
) {
    private val procChance = 0.10
    private val healMultiplier = 0.5

    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val attacker = event.damager as? Player ?: return
        val target = event.entity as? LivingEntity ?: return
        val damage = event.damage


        if (Random.nextDouble() > procChance) return
        val healAmount = damage * healMultiplier
        val maxHp = attacker.getAttribute(Attribute.MAX_HEALTH)?.value ?: return

        attacker.safeHeal((attacker.health + healAmount).coerceAtMost(maxHp))

        attacker.world.spawnParticle(
            Particle.DAMAGE_INDICATOR,
            attacker.location.add(0.0, 1.0, 0.0),
            10,
            0.3, 0.4, 0.3,
            0.1
        )

        attacker.actionMsg(
            "<pink>Меч Кровопийца</pink> <gray>восстановил <red>${"%.1f".format(healAmount)}</red> ❤</gray>"
        )
    }
}
