package dev.elysium.eraces.items.колчаны

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry

object КолчанManager {
    fun enable(plugin: ERaces) {
        val колчаны = listOf<Quivers>(
            BeerQuiver()
        )

        колчаны.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(КолчаныListener(), plugin)
    }
}