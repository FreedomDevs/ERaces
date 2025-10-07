package dev.elysium.eraces.config;

import dev.elysium.eraces.datatypes.SpecializationPlayerData;
import dev.elysium.eraces.utils.SqliteDatabase;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SpecializationsManager {
    @Getter
    private final SpecializationsConfigManager specializationsConfigManager;
    private final SqliteDatabase database;

    private final Map<UUID, SpecializationPlayerData> specPlayersData = new HashMap<>();
    private final Set<UUID> updatedPlayers = new HashSet<>();

    private final Object flushPlayerDataLock = new Object();
    public void flushPlayerDataCache() {
        synchronized (flushPlayerDataLock) {
            if (!updatedPlayers.isEmpty()) {
                try (Connection conn = database.getConnection()) {
                    conn.setAutoCommit(false);
                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT OR REPLACE INTO specialization_levels(uuid, level, xp, int, str, agi, vit) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    );

                    for (UUID uuid : updatedPlayers) {
                        SpecializationPlayerData p = specPlayersData.get(uuid);
                        stmt.setString(1, uuid.toString());
                        stmt.setLong(2, p.getLevel());
                        stmt.setLong(3, p.getXp());
                        stmt.setDouble(4, p.getINT());
                        stmt.setDouble(5, p.getSTR());
                        stmt.setDouble(6, p.getAGI());
                        stmt.setDouble(7, p.getVIT());
                        stmt.addBatch();
                    }

                    stmt.executeBatch();
                    conn.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                updatedPlayers.clear();
            }
        }
    }
    private void addPlayerToUpdated(UUID uuid) {
        synchronized (flushPlayerDataLock) {
            updatedPlayers.add(uuid);
        }
    }

    public SpecializationsManager(JavaPlugin plugin, SqliteDatabase database) {
        specializationsConfigManager = new SpecializationsConfigManager(plugin);
        this.database = database;
        reloadConfig();
    }

    public void reloadConfig() {
        specializationsConfigManager.reloadConfig();
    }

    private void loadSpecPlayerData() {
        String query = "SELECT uuid, level, xp, int, str, agi, vit FROM players";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));

                SpecializationPlayerData data = new SpecializationPlayerData();
                data.setLevel(rs.getLong("level"));
                data.setXp(rs.getLong("xp"));
                data.setINT(rs.getDouble("int"));
                data.setSTR(rs.getDouble("str"));
                data.setAGI(rs.getDouble("agi"));
                data.setVIT(rs.getDouble("vit"));

                specPlayersData.put(uuid, data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // можно логировать или бросать RuntimeException
        }
    }
}
