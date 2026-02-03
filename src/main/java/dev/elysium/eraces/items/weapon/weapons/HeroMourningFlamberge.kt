package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.eParticle.EParticle
import dev.elysium.eraces.utils.targetUtils.safeDamage
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class HeroMourningFlamberge(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "hero_mourning_flamberge",
    material = Material.IRON_SWORD,
    name = "<pink>Фламберг Скорбь Героя",
    damage = 8.0,
    attackSpeed = 1.7,
    maxDurability = 1350,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 8.0,
            attackSpeed = 1.7,
            passiveEffects = listOf(
                "<gradient:#b30000:#ff4d4d>Пассив: Кровотечение</gradient>" to listOf(
                    "<gray>▸ <white>Каждый удар накладывает",
                    "<gray>▸ <red>кровотечение</red> на <white>2 секунды</white>"
                )
            )
        )
    )
) {

    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val target = event.entity as? LivingEntity ?: return
        applyBleeding(target)

    }

    private fun applyBleeding(target: LivingEntity) {
        val uuid = target.uniqueId

        bleedingTasks[uuid]?.cancel()

        val task = object : BukkitRunnable() {
            var ticks = 0

            override fun run() {
                if (target.isDead || target.isValid) {
                    cancel()
                    return
                }

                target.safeDamage(1.0)
                EParticle.damageIndicator(target.world, target.location.add(0.0, 1.0, 0.0))
                ticks++
                if (ticks >=4 ) {
                    cancel()
                }
            }

            override fun cancel() {
                super.cancel()
                bleedingTasks.remove(uuid)
            }
        }

        bleedingTasks[uuid] = task
        task.runTaskTimer(plugin, 0L, 10L)
    }

    companion object {
        private val bleedingTasks = ConcurrentHashMap<UUID, BukkitRunnable>()
    }

}
