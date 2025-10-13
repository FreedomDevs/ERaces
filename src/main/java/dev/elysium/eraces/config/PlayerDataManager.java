package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.utils.SqliteDatabase;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDataManager {
    private final Map<String, Race> races;
    private final SqliteDatabase database;


    public PlayerDataManager(Map<String, Race> races, SqliteDatabase database) {
        this.database = database;
        this.races = ERaces.getInstance().getContext().racesConfigManager.getRaces();
    }

    public String getPlayerRaceId(Player player) {
        try {
            return getPlayerRaceIdAsync(player).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> getPlayerRaceIdAsync(Player player) {
        UUID uuid = player.getUniqueId();

        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement ps = database.getConnection().prepareStatement(
                    "SELECT race_id FROM races WHERE uuid = ?"
            )) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String raceId = rs.getString("race_id");
                    rs.close();
                    return raceId;
                } else {
//                    String randomRaceId = getRandomRaceId();
                    rs.close();
//                    setPlayerRaceAsync(player, randomRaceId);
                    return null; // Чтобы мы могли открыть гуи
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Race getPlayerRace(Player player) {
        try {
            return getPlayerRaceAsync(player).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Race> getPlayerRaceAsync(Player player) {
        return getPlayerRaceIdAsync(player).thenApply(raceId -> {
            if (raceId == null) return null;
            return races.get(raceId);
        });
    }


    public void setPlayerRace(Player player, String raceId) {
        try {
            setPlayerRaceAsync(player, raceId).get();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Void> setPlayerRaceAsync(Player player, String raceId) {
        if (!races.containsKey(raceId))
            throw new RuntimeException("Данная раса не существует! (" + raceId + ")");

        UUID uuid = player.getUniqueId();
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement ps = database.getConnection().prepareStatement(
                    "INSERT INTO races(uuid, race_id) VALUES (?, ?) " +
                            "ON CONFLICT(uuid) DO UPDATE SET race_id = excluded.race_id"
            )) {
                ps.setString(1, uuid.toString());
                ps.setString(2, raceId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public String getRandomRaceId() {
        List<String> raceIds = this.races.entrySet().stream()
                .filter(entry -> !entry.getValue().isExcludeFromRandom())
                .map(Map.Entry::getKey)
                .toList();

        if (raceIds.isEmpty())
            throw new NullPointerException("Нету рас для рандомной выдачи");

        return raceIds.get(ThreadLocalRandom.current().nextInt(raceIds.size()));
    }
}
