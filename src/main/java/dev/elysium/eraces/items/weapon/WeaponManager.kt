package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry

object WeaponManager {
    fun enable(plugin: ERaces, weapons: List<Weapon>) {
        weapons.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(WeaponListener(), plugin)
    }
}