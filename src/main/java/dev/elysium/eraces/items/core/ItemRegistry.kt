package dev.elysium.eraces.items.core

import dev.elysium.eraces.ERaces
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object ItemRegistry {
    private val items = mutableMapOf<String, Item>()

    fun register(item: Item) {
        items[item.id] = item

        if (item is Listener)
            Bukkit.getPluginManager().registerEvents(item as Listener, ERaces.getInstance())
    }

    fun byId(id: String): Item? = items[id]

    fun all(): Collection<Item> = items.values
}