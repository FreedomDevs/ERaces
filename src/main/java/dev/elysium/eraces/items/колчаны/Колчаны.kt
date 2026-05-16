package dev.elysium.eraces.items.колчаны

import dev.elysium.eraces.gui.GuiHolder
import dev.elysium.eraces.gui.GuiType
import dev.elysium.eraces.items.core.Item
import dev.elysium.eraces.items.core.ItemType
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.utils.ChatUtil
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemLore
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
open class Колчаны(
    override val id: String,
    val name: String,
    val lore: ItemLore,
    val arrowSlots: Int,
    val additionalDamage: Double = 0.0
) : Item {
    final override val type = ItemType.КОЛЧАНЫ

    override fun onInit(item: ItemStack) {
        item.setData(DataComponentTypes.CUSTOM_NAME, ChatUtil.parse(name));

        item.setData(DataComponentTypes.LORE, lore)

        item.setData(
            DataComponentTypes.CUSTOM_MODEL_DATA,
            CustomModelData.customModelData().addString(id).build()
        );
    }

    open fun onHit(event: EntityDamageByEntityEvent) {
        event.damage += 1
    }

    fun openКолчаныGui(player: Player) {
        val size = 18
        val holder = GuiHolder(GuiType.QUIVER, name)
        val inv: Inventory = Bukkit.createInventory(player, size, ChatUtil.parse(name))
        holder.setInventory(inv)

        val barrier = ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta.apply {
                isHideTooltip = true
            }
        }
        val currentArrows = ItemState(player.inventory.itemInMainHand).getInt(StateKeys.ARROWS_КОЛЧАН)

        var remaining = currentArrows
        var slot = 0

        while (remaining > 0 && slot < arrowSlots) {
            val amount = if (remaining >= 64) 64 else remaining
            val arrowItem = ItemStack(Material.ARROW, amount)

            inv.setItem(slot, arrowItem)
            remaining -= amount
            slot++
        }

        for (i in arrowSlots until size) {
            inv.setItem(i, barrier)
        }
    }
}