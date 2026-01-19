package dev.elysium.eraces.items.core

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemFactory {
    fun create(item:  Item, base: ItemStack): ItemStack {
        val stack  =  base.clone()
        val meta = stack.itemMeta ?: return stack

        val pdc = meta.persistentDataContainer
        pdc.set(ItemKeys.ITEM_ID, PersistentDataType.STRING, item.id)
        pdc.set(ItemKeys.ITEM_TYPE, PersistentDataType.STRING, item.type.name)

        stack.itemMeta = meta
        item.onInit(stack)
        return stack
    }
}