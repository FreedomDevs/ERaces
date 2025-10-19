package dev.elysium.eraces

import dev.elysium.eraces.config.*
import dev.elysium.eraces.utils.SqliteDatabase
import dev.elysium.eraces.xpManager.XpManager

class PluginContext(val database: SqliteDatabase) {
    lateinit var globalConfigManager: GlobalConfigManager
    lateinit var messageManager: MessageManager
    lateinit var racesConfigManager: RacesConfigManager
    lateinit var playerDataManager: PlayerDataManager
    lateinit var specializationsManager: SpecializationsManager
    lateinit var xpManager: XpManager
    lateinit var xpDamageTracker: DamageTracker
    lateinit var manaManager: ManaManager
}