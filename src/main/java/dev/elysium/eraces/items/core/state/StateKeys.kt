package dev.elysium.eraces.items.core.state

import dev.elysium.eraces.ERaces
import org.bukkit.NamespacedKey

object StateKeys {
    lateinit var HITS: NamespacedKey

    fun init(plugin: ERaces) {
        HITS = NamespacedKey(plugin, "hits")
    }
}