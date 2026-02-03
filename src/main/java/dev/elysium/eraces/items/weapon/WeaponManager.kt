package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.weapon.weapons.*

object WeaponManager {
    fun enable(plugin: ERaces) {
        val weapons = listOf(
            CombatStaff(plugin),
            Dagger(plugin),
            IronSword(plugin),
            OneHandedSword(plugin),
            DamagedHalberd(plugin),
            Scythes(plugin),
            Blade(plugin),
            Halberd(plugin),
            WhisperingSickles(plugin),
            BloodSickles(plugin),
            BloodDrinkerGreatsword(plugin),
            DawnDao(plugin),

        )

        weapons.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(WeaponListener(), plugin)

        WeaponInventoryTask().runTaskTimer(plugin, 0L, 5L)
    }
}