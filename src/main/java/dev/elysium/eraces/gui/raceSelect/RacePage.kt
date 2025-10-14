package dev.elysium.eraces.gui.raceSelect

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class RacePage(
    val id: String,
    val displayName: String,
    val lore: List<String>,
    val material: Material,
    val title: String
) {
    fun toItem(): ItemStack {
        return ItemStack(material).apply {
            val meta = itemMeta ?: return@apply

            meta.displayName(Component.text(displayName, TextColor.color(0xFFFF55)))
            meta.lore(lore?.map { Component.text(it, TextColor.color(0xAAAAAA)) })

            itemMeta = meta
        }
    }

}

