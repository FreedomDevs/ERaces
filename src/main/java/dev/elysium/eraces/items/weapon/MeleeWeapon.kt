package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
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
    val maxDurability: Int,
    val options: Map<String, Any> = emptyMap()
) : Weapon() {
    abstract val plugin: ERaces

    override fun onInit(item: ItemStack) {
        val meta = item.itemMeta ?: return

        meta.displayName(ChatUtil.parse(name))
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

        item.itemMeta = meta

        val state = ItemState(item)

        if (!state.contains(StateKeys.DURABILITY)) {
            state.setInt(StateKeys.DURABILITY, maxDurability)
        }
        if (!state.contains(StateKeys.HITS)) {
            state.setInt(StateKeys.HITS, 0)
        }

        updateLore(item)
    }

    override fun onHit(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        val weapon = ItemResolver.resolve(stack) as? MeleeWeapon ?: return
        if (weapon.id != this.id) return

        val state = ItemState(stack)
        val hits = state.addInt(StateKeys.HITS, 1)
        val max = state.getInt(StateKeys.DURABILITY)

        updateLore(stack)

        if (hits >= max) {
            player.sendMessage("Ваш ${stack.itemMeta?.displayName} сломался!")
            resolveBrokenItem(player, stack)
        }

        val critChance = options["critChance"] as? Double ?: 0.0
        if (Math.random() < critChance) {
            event.damage *= 2
            player.sendMessage(ChatUtil.parse("&6Критический удар!"))
        }
    }

    private fun updateLore(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val state = ItemState(item)
        val hits = state.getInt(StateKeys.HITS)
        val max = state.getInt(StateKeys.DURABILITY)

        val loreLines = (options["lore"] as? List<*>)?.mapNotNull {
            it as? String
        } ?: emptyList()

        val updateLore = loreLines.map { line ->
            ChatUtil.parse(line, mapOf(
                "{hits}" to hits.toString(),
                "{durability}" to max.toString(),
                "{bar}" to getDurabilityBar(hits, max),
                "{current_durability}" to (max - hits).toString()
            ))
        }

        meta.lore(updateLore)
        item.itemMeta = meta

    }

    private fun resolveBrokenItem(player: Player, stack: ItemStack) {
        val inv = player.inventory

        for (slot in 0 until inv.size) {
            val current = inv.getItem(slot) ?: continue

            if (current.isSimilar(stack)) {
                inv.setItem(slot, null)
                return
            }
        }

        if (inv.itemInOffHand.isSimilar(stack)) {
            inv.setItemInOffHand(null)
        }
    }

    private fun getDurabilityBar(hits: Int, max: Int, length: Int = 10): String {
        val safeHits = hits.coerceAtMost(max)
        val percent = safeHits.toDouble() / max
        val filled = (length * percent).toInt()
        val empty = (length - filled).coerceAtLeast(0)

        val barFilled = "■".repeat(filled)
        val barEmpty = "■".repeat(empty)

        val bar = "<red>$barFilled<green>$barEmpty"

        return bar
    }
}
