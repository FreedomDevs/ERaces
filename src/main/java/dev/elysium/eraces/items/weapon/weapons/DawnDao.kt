package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.actionMsg
import dev.elysium.eraces.utils.eParticle.EParticle
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable

class DawnDao(override val plugin: ERaces) : MeleeWeapon(
    id = "dawn_dao",
    material = Material.IRON_SWORD,
    name = "<pink>Дао Рассвета",
    damage = 8.0,
    attackSpeed = 1.7,
    maxDurability = 1000,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 8.0,
            attackSpeed = 1.7,
            activeAbilities = listOf(
                "<gradient:#ffd54f:#ff9800>Способность: Прокалывание</gradient>" to listOf(
                    "<gray>▸ <white>[ПКМ] — <yellow>12</yellow> урона",
                    "<gray>▸ <white>Кровотечение <red>7 сек</red>"
                )
            )
        )
    )
) {
    private val cooldownMillis = 6000L
    private val range = 4.5
    private val directDamage = 12.0
    private val bleedDurationTicks = TimeParser.parseToTicks("7s").toInt()
    private val bleedTickDamage = 1.0
    private val bleedInterval = 20L

    override fun onInteract(player: Player, hand: EquipmentSlot, click: ClickType) {
        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand

        tryAbility(player, stack, cooldownMillis) {
            val target = findTarget(player) ?: run {
                player.actionMsg("<gray>Нет цели в досягаемости…</gray>")
                return@tryAbility
            }

            pierce(player, target)
        }
    }


    private fun findTarget(player: Player): LivingEntity? {
        val result = player.world.rayTraceEntities(
            player.eyeLocation,
            player.eyeLocation.direction,
            range
        ) { it is LivingEntity && it != player }

        return result?.hitEntity as? LivingEntity
    }

    private fun pierce(player: Player, target: LivingEntity) {
        target.damage(directDamage, player)

        EParticle.crit(target.world, target)

        object : BukkitRunnable() {
            var ticksPassed = 0

            override fun run() {
                if (target.isDead || ticksPassed >= bleedDurationTicks) {
                    cancel()
                    return
                }

                target.damage(bleedTickDamage, player)

                EParticle.damageIndicator(
                    target.world,
                    target.location.clone().add(0.0, target.height * 0.5, 0.0)
                )


                ticksPassed += bleedInterval.toInt()
            }
        }.runTaskTimer(plugin, bleedInterval, bleedInterval)
    }
}