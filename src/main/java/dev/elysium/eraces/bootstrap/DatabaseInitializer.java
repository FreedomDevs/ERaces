package dev.elysium.eraces.bootstrap;


import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.exceptions.InitFailedException;
import dev.elysium.eraces.utils.SqliteDatabase;

import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        plugin.getDataFolder().mkdirs();
        Path dbPath = plugin.getDataFolder().toPath().resolve("database_sqlite.db");
        SqliteDatabase database = new SqliteDatabase();
        ERaces.getInstance().getContext().setDatabase(database);
        database.connect(dbPath.toString());

        try (Statement stmt = database.getConnection().createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS races (
                    uuid TEXT PRIMARY KEY,
                    race_id TEXT
                );
            """);

            stmt.executeUpdate("""
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
            """);
        } catch (SQLException e) {
            throw new InitFailedException("Не удалось создать таблицы БД", e);
        }
    }
}
