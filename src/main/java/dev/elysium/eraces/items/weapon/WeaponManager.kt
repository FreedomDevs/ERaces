package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.weapon.services.WeaponInventoryService
import dev.elysium.eraces.items.weapon.services.WeaponTickService
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
            TwoHandedAxe(plugin),
            MurasuSpear(plugin),
            HeroMourningFlamberge(plugin),
            SplitChakrams(plugin)
        )

        weapons.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(WeaponListener(), plugin)

        WeaponInventoryService.init(plugin)
        WeaponTickService.init(plugin)

    }
}