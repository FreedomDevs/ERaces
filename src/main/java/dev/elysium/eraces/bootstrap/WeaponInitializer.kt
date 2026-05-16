package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemKeys
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.WeaponManager

class WeaponInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        ItemKeys.init(plugin)
        StateKeys.init(plugin)
        WeaponManager.enable(plugin)
    }
}