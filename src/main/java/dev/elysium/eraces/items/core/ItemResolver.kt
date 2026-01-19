package dev.elysium.eraces.items.core

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemResolver {
    fun resolve(stack: ItemStack?): Item? {
        val meta = stack?.itemMeta ?: return null
        val id = meta.persistentDataContainer.get(
            ItemKeys.ITEM_ID,
            PersistentDataType.STRING
        ) ?: return null

        return ItemRegistry.byId(id)
    }
}
