package dev.elysium.eraces.gui.core

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
                meta.displayName(Component.text(name))
                itemMeta = meta
            }
            return GuiButton(item, onClick)
        }
    }
}