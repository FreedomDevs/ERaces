package dev.elysium.eraces.items.core

import org.bukkit.inventory.ItemStack

interface Item {
    val id: String
    val type: ItemType

    fun onInit(item: ItemStack)
}