package dev.elysium.eraces.gui.core

import dev.elysium.eraces.utils.ChatUtil
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * Кнопка для GUI.
 */
class GuiButton(
    val item: ItemStack,
    val onClick: (InventoryClickEvent) -> Unit
) {
    companion object {
        fun of(material: Material, name: String, onClick: (InventoryClickEvent) -> Unit): GuiButton {
            val item = ItemStack(material).apply {
                val meta = itemMeta ?: return@apply
                meta.displayName(ChatUtil.parse(name))
                itemMeta = meta
            }
            return GuiButton(item, onClick)
        }
    }
}