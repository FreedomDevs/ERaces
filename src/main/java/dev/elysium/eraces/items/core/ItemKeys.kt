package dev.elysium.eraces.items.core

import dev.elysium.eraces.ERaces
import org.bukkit.NamespacedKey

object ItemKeys {
    lateinit var ITEM_ID: NamespacedKey
    lateinit var ITEM_TYPE: NamespacedKey

    fun init(plugin: ERaces) {
        ITEM_ID = NamespacedKey(plugin, "item_id")
        ITEM_TYPE = NamespacedKey(plugin, "item_type")
    }
}