package dev.elysium.eraces.items.core

object ItemRegistry {
    private val items = mutableMapOf<String, Item>()

    fun register(item: Item) {
        items[item.id] = item
    }

    fun byId(id: String): Item? = items[id]
}