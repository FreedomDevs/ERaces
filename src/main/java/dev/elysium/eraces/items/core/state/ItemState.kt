package dev.elysium.eraces.items.core.state

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ItemState(private val stack: ItemStack) {
    private val meta = stack.itemMeta ?: error("Item has no meta")
    private val pdc = meta.persistentDataContainer

    fun getInt(key: NamespacedKey, default: Int = 0): Int {
        return pdc.get(key, PersistentDataType.INTEGER) ?: default
    }

    fun setInt(key: NamespacedKey, value: Int) {
        pdc.set(key, PersistentDataType.INTEGER, value)
        stack.itemMeta = meta
    }

    fun addInt(key: NamespacedKey, delta: Int): Int {
        val new = getInt(key) + delta
        setInt(key, new)
        return new
    }

    fun contains(key: NamespacedKey): Boolean {
        val meta = stack.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.INTEGER)
    }

    fun getLong(key: NamespacedKey, default: Long = 0L): Long {
        return pdc.get(key, PersistentDataType.LONG) ?: default
    }

    fun setLong(key: NamespacedKey, value: Long) {
        pdc.set(key, PersistentDataType.LONG, value)
        stack.itemMeta = meta
    }
}
