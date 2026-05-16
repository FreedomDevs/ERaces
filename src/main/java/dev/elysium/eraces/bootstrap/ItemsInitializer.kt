package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemKeys
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.WeaponManager
import dev.elysium.eraces.items.колчаны.КолчанManager

class ItemsInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        ItemKeys.init(plugin)
        StateKeys.init(plugin)
        WeaponManager.enable(plugin)
        КолчанManager.enable(plugin)
    }
}