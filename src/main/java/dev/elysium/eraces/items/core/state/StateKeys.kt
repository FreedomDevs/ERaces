package dev.elysium.eraces.items.core.state

import dev.elysium.eraces.ERaces
import org.bukkit.NamespacedKey

object StateKeys {
    lateinit var HITS: NamespacedKey
    lateinit var DURABILITY: NamespacedKey
    lateinit var KD: NamespacedKey
    lateinit var MANA: NamespacedKey

    fun init(plugin: ERaces) {
        HITS = NamespacedKey(plugin, "hits")
        DURABILITY = NamespacedKey(plugin, "max_hits")
        KD = NamespacedKey(plugin, "kd")
        MANA = NamespacedKey(plugin, "mana")
    }
}