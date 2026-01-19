package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

abstract class MeleeWeapon(
    override val id: String,
    val material: Material,
    private val model: Int,
    private val name: String,
    val damage: Double,
    val attackSpeed: Double,
    private val isUnbreakable: Boolean = false,
    val maxDurability: Int
) : Weapon() {
    abstract val plugin: ERaces

    override fun onInit(item: ItemStack) {
        val meta = item.itemMeta ?: return

        meta.displayName(Component.text(name))
        meta.setCustomModelData(model)
        meta.isUnbreakable = isUnbreakable


        val damageModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_damage"),
            damage,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)


        val speedModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_speed"),
            attackSpeed - 4.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedModifier)

        val state = ItemState(item)
        state.setInt(StateKeys.HITS, maxDurability)


        item.itemMeta = meta
    }

    override fun onHit(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        val weapon = ItemResolver.resolve(stack) as? MeleeWeapon ?: return
        if (weapon.id != this.id) return

        val remaining = ItemState(stack).addInt(StateKeys.HITS, -1)
        if (remaining <= 0) {
            stack.amount = 0
            player.sendMessage("Ваш ${stack.itemMeta?.displayName} сломался!")
        }
    }
}
