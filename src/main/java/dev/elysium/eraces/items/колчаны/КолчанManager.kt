package dev.elysium.eraces.items.колчаны

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.core.ItemRegistry.register
import kotlin.collections.forEach

object КолчанManager {
    fun enable(plugin: ERaces) {
        val колчаны = listOf<Колчаны>(
            BeerКолчан()
        )

        колчаны.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(КолчаныListener(), plugin)
    }
}