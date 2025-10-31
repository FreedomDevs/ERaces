package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.SpecializationPlayerData;
import dev.elysium.eraces.datatypes.configs.SpecializationData;
import dev.elysium.eraces.utils.SqliteDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SpecializationsManager {
    @Getter
    private final SpecializationsConfigManager config;
    private final SqliteDatabase database;

    public final Map<UUID, SpecializationPlayerData> specPlayersData = new HashMap<>(); // НИ В КОЕМ СЛУЧАЕ НЕ РЕДАКТИРОВАТЬ ИЗ ВНЕ (читать можна)
    private final Set<UUID> updatedPlayers = new HashSet<>();

    private final Object flushPlayerDataLock = new Object();

    private static final long MAX_LEVEL = 50;

    private Integer task;

    public void flushPlayerDataCache() {
        synchronized (flushPlayerDataLock) {
            if (!updatedPlayers.isEmpty()) {
                Connection conn = database.getConnection();
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT OR REPLACE INTO specialization_levels(uuid, specialization, level, xp, int, str, agi, vit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    );

                    for (UUID uuid : updatedPlayers) {
                        SpecializationPlayerData p = specPlayersData.get(uuid);
                        stmt.setString(1, uuid.toString());
                        stmt.setString(2, p.getSpecialization());
                        stmt.setLong(3, p.getLevel());
                        stmt.setLong(4, p.getXp());
                        stmt.setDouble(5, p.getINT());
                        stmt.setDouble(6, p.getSTR());
                        stmt.setDouble(7, p.getAGI());
                        stmt.setDouble(8, p.getVIT());
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
        config = new SpecializationsConfigManager(plugin);
        config.getXpNext(1);config.getPointsPerLevel(1);// Чтобы сразу проверить на ошибки
        this.database = database;
        reloadConfig();
        loadSpecPlayerData();
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::flushPlayerDataCache, 20, 20).getTaskId();
    }

    public void reloadConfig() {
        config.reloadConfig();
    }

    private void upgradeLevel(UUID uuid, SpecializationPlayerData data) {
        long points = config.getPointsPerLevel(data.getLevel());
        SpecializationData spec = config.specializations.getOrDefault(data.getSpecialization(), new SpecializationData());
        if (!config.specializations.containsKey(data.getSpecialization()))
            ERaces.getInstance().getLogger().warning("Unknown specialization: " + data.getSpecialization());

        data.setSTR(data.getSTR() + points * (spec.getStrength() / 100.0));
        data.setINT(data.getINT() + points * (spec.getIntelligent() / 100.0));
        data.setAGI(data.getAGI() + points * (spec.getAgility() / 100.0));
        data.setVIT(data.getVIT() + points * (spec.getVitality() / 100.0));

        data.setXp(data.getXp() - config.getXpNext(data.getLevel()));
        data.setLevel(data.getLevel() + 1);
        addPlayerToUpdated(uuid);
    }

    private void tryToUpgradeLevel(UUID uuid, SpecializationPlayerData data) {
        while (data.getLevel() < MAX_LEVEL && data.getXp() >= config.getXpNext(data.getLevel())) {
            upgradeLevel(uuid, data);
        }

        if (data.getLevel() >= MAX_LEVEL) {
            data.setLevel(MAX_LEVEL);
            data.setXp(0);
        }
    }

    public @NotNull SpecializationPlayerData getPlayerData(OfflinePlayer player) {
        return specPlayersData.get(player.getUniqueId());
    }
    public void setPlayerSpecialization(OfflinePlayer player, String specializaton) {
        specPlayersData.get(player.getUniqueId()).setSpecialization(specializaton);
        updatedPlayers.add(player.getUniqueId());
    }

    public void ensurePlayerInitialized(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!specPlayersData.containsKey(uuid)) {
            SpecializationPlayerData data = new SpecializationPlayerData();
            data.setSpecialization("");
            data.setLevel(1);
            data.setXp(0);
            data.setINT(0);
            data.setSTR(0);
            data.setAGI(0);
            data.setVIT(0);

            specPlayersData.put(uuid, data);
            addPlayerToUpdated(uuid);
        }
    }

    public void addXp(OfflinePlayer player, long xp) {
        SpecializationPlayerData data = specPlayersData.get(player.getUniqueId());
        if (data.getLevel() >= MAX_LEVEL) return;
        data.setXp(data.getXp() + xp);
        addPlayerToUpdated(player.getUniqueId());
        tryToUpgradeLevel(player.getUniqueId(), data);
    }

    private void loadSpecPlayerData() {
        String query = "SELECT uuid, specialization, level, xp, int, str, agi, vit FROM specialization_levels";

        Connection conn = database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));

                SpecializationPlayerData data = new SpecializationPlayerData();
                data.setSpecialization(rs.getString("specialization"));
                data.setLevel(rs.getLong("level"));
                data.setXp(rs.getLong("xp"));
                data.setINT(rs.getDouble("int"));
                data.setSTR(rs.getDouble("str"));
                data.setAGI(rs.getDouble("agi"));
                data.setVIT(rs.getDouble("vit"));

                specPlayersData.put(uuid, data);
                tryToUpgradeLevel(uuid, data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // можно логировать или бросать RuntimeException
        }
    }
}
