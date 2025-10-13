package dev.elysium.eraces

import dev.elysium.eraces.config.*
import dev.elysium.eraces.utils.SqliteDatabase

class PluginContext(val database: SqliteDatabase) {
    lateinit var globalConfigManager: GlobalConfigManager
    lateinit var messageManager: MessageManager
    lateinit var racesConfigManager: RacesConfigManager
    lateinit var playerDataManager: PlayerDataManager
    lateinit var specializationsManager: SpecializationsManager
}