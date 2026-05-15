package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CrackedBlade : MeleeWeapon(
    id = "cracked_blade",
    material = Material.IRON_SWORD,
    name = "<#FF3399>Треснутый Клинок",
    damage = 12.0,
    attackSpeed = 1.7,
    maxDurability = 1570,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 12.0,
            attackSpeed = 1.7,
            passiveEffects = listOf(
                "<aqua>Накопление энергии</aqua>" to listOf(
                    "<gray>▸ <white>Каждый удар накапливает <yellow>1</yellow> единицу маны"
                )
            ),
            activeAbilities = listOf(
                "<gradient:#ff6666:#ffcc66>Способность: Сильный удар</gradient>" to listOf(
                    "<gray>▸ <white>Требует <#ffb347>20</#ffb347> маны",
                    "<gray>▸ <white>Наносит <red>20</red> урона"
                )
            ),
            useMana = true
        )
    )
) {
    fun onHitLogic(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        val state = ItemState(stack)
        val currentMana = state.addInt(StateKeys.MANA, 1)

        player.actionMsg("У вас сейчас $currentMana/20 маны")
        if (currentMana >= 21) {
            state.setInt(StateKeys.MANA, 0)

            val target = event.entity as? LivingEntity ?: return

            target.damage(20.0, player)
        }
    }

    override fun onHit(event: EntityDamageByEntityEvent) {
        onHitLogic(event)
        super.onHit(event)
    }
}