package dev.elysium.eraces

import dev.elysium.eraces.config.GlobalConfigManager
import dev.elysium.eraces.config.MessageManager
import dev.elysium.eraces.config.PlayerDataManager
import dev.elysium.eraces.config.RacesConfigManager
import dev.elysium.eraces.utils.SqliteDatabase

class PluginContext(val database: SqliteDatabase) {
    lateinit var globalConfigManager: GlobalConfigManager
    lateinit var messageManager: MessageManager
    lateinit var racesConfigManager: RacesConfigManager
    lateinit var playerDataManager: PlayerDataManager
}