package dev.elysium.eraces.gui

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

data class RacePage(
    val id: String,
    val displayName: String,
    val description: List<String>,
    val icon: Material
) {
    fun toItem(): ItemStack {
        val item = ItemStack(icon)
        val meta: ItemMeta = item.itemMeta!!
        meta.setDisplayName("§a${displayName}")
        meta.lore = description.map { "§7$it" }
        item.itemMeta = meta
        return item
    }
}
