package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.weapon.services.WeaponInventoryService
import dev.elysium.eraces.items.weapon.services.WeaponTickService
import dev.elysium.eraces.items.weapon.weapons.*

object WeaponManager {
    fun enable(plugin: ERaces) {
        val weapons = listOf(
            CombatStaff(),
            Dagger(),
            IronSword(),
            OneHandedSword(),
            DamagedHalberd(),
            Scythes(),
            Blade(),
            Halberd(),
            WhisperingSickles(),
            BloodSickles(),
            BloodDrinkerGreatsword(),
            DawnDao(),
            TwoHandedAxe(),
            MurasuSpear(),
            HeroMourningFlamberge(),
            SplitChakrams(),
            CrackedBlade(),
            HopeRapier(),
            WarTearsAxe(),
        )

        weapons.forEach(ItemRegistry::register)
        plugin.server.pluginManager.registerEvents(WeaponListener(), plugin)

        WeaponInventoryService.init(plugin)
        WeaponTickService.init(plugin)

    }
}