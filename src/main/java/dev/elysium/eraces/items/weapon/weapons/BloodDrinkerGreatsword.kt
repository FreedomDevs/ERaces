package dev.elysium.eraces.items.weapon.weapons

import org.bukkit.attribute.Attribute
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.eParticle.EParticle
import dev.elysium.eraces.utils.targetUtils.safeHeal
import org.bukkit.Material
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
        "lore" to WeaponLoreBuilder.build(
            damage = 10.0,
            attackSpeed = 1.7,
            passiveEffects = listOf(
                "<gradient:#ff3b3b:#7a0000>Пассивное действие: Поглотитель кровей</gradient>" to listOf(
                    "<gray>▸ <white>10% шанс восстановить",
                    "<gray>▸ <red>50%</red> от нанесённого урона"
                )
            )
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

        EParticle.damageIndicator(attacker.world, attacker.location)

        attacker.actionMsg(
            "<pink>Меч Кровопийца</pink> <gray>восстановил <red>${"%.1f".format(healAmount)}</red> ❤</gray>"
        )
    }
}
