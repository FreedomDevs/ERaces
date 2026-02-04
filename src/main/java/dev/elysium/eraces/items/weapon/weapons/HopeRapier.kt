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

class HopeRapier(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "hope_rapier",
    material = Material.IRON_SWORD,
    name = "<pink>Шпага «Завет надежды»",
    damage = 8.0,
    attackSpeed = 2.5,
    maxDurability = 600,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 8.0,
            attackSpeed = 2.5,
            activeAbilities = listOf(
                "<gradient:#ff6699:#ff33cc>Шквал отчаяния</gradient>" to listOf(
                    "<gray>▸ <white>[ПКМ] — Наносит 6 ударов по <red>4</red> урона",
                    "<gray>▸ <white>Не может оставить цель с менее чем 25% HP"
                )
            )
        )
    )
) {
    private val abilityCooldownMillis = 5000L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT || hand != EquipmentSlot.HAND) return
        val stack = player.inventory.itemInMainHand

        tryAbility(player, stack, abilityCooldownMillis) {
            whirlwind(player)
        }
    }

    private fun whirlwind(player: Player) {
        val world = player.world
        val start = player.eyeLocation.clone()
        val direction = start.direction.normalize()
        val maxDistance = 3.0
        val hitRadius = 1.0
        val hits = 6
        val damage = 4.0

        EParticle.lineEffect(world, start, direction, maxDistance, step = 0.2, particle = Particle.SWEEP_ATTACK)
        EParticle.lineEffect(world, start, direction, maxDistance, step = 0.2, particle = Particle.CLOUD)

        var hitsDone = 0

        while (hitsDone < hits) {
            val result = world.rayTraceEntities(start, direction, maxDistance, hitRadius) { entity ->
                entity is LivingEntity && entity != player && entity.health > entity.maxHealth * 0.25
            }
            val target = result?.hitEntity as? LivingEntity ?: break

            val maxDamage = target.health - target.maxHealth * 0.25
            val appliedDamage = damage.coerceAtMost(maxDamage)
            if (appliedDamage > 0) target.damage(appliedDamage, player)

            EParticle.crit(world, target, 10)

            hitsDone++
        }
    }
}