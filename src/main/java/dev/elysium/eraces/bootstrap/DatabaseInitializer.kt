package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.exceptions.internal.InitFailedException
import dev.elysium.eraces.utils.SqliteDatabase
import java.sql.SQLException


class DatabaseInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        plugin.dataFolder.mkdirs()

        val dbPath = plugin.dataFolder.toPath().resolve("database_sqlite.db")
        val database = SqliteDatabase()
        getInstance().context.database = database
        database.connect(dbPath.toString())

        try {
            database.connection.createStatement().use { stmt ->
                stmt.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS races (
                    uuid TEXT PRIMARY KEY,
                    race_id TEXT
                );
                """
                )
                stmt.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS specialization_levels (
                    uuid TEXT PRIMARY KEY,
                    specialization TEXT NOT NULL,
                    level INTEGER NOT NULL,
                    xp INTEGER NOT NULL,
                    int REAL NOT NULL,
                    str REAL NOT NULL,
                    agi REAL NOT NULL,
                    vit REAL NOT NULL
                );
                """
                )
            }
        } catch (e: SQLException) {
            throw InitFailedException("Не удалось создать таблицы БД", e)
        }
    }
}
