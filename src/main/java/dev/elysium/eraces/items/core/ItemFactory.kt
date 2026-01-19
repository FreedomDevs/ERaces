package dev.elysium.eraces.items.core

import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemFactory {
    fun create(item: Item): ItemStack {
        val stack = ItemStack(
            when(item) {
                is MeleeWeapon -> item.material
                else -> Material.STONE
            }
        )

        val meta = stack.itemMeta ?: return stack
        val pdc = meta.persistentDataContainer

        pdc.set(ItemKeys.ITEM_ID, PersistentDataType.STRING, item.id)
        pdc.set(ItemKeys.ITEM_TYPE, PersistentDataType.STRING, item.type.name)
        stack.itemMeta = meta

        item.onInit(stack)
        return stack
    }
}
