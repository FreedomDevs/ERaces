package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDataManager {
    private static final String FILE_NAME = "playerData.yml";
    private final YamlManager cfgManager;
    private YamlConfiguration playerDataYaml;
    private final Map<UUID, Race> playerRaceMap = new HashMap<>();
    private final Map<String, Race> races;
    private final ERaces plugin;


    public PlayerDataManager(ERaces plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(plugin, FILE_NAME, false);
        this.races = ERaces.getRacesMng().getRaces();
        loadData();
    }

    public void loadData() {
        playerDataYaml = cfgManager.getConfig();
        for (String uuid : playerDataYaml.getKeys(false)) {
            String raceId = playerDataYaml.getString(uuid);

            if (!this.races.containsKey(raceId)) {
                plugin.getLogger().severe("ВНИМАНИЕ: РАСА: " + raceId + " КУДАТ ПРОПАЛА (перегенерация...)");
                raceId = getRandomRaceId();
            }
            playerRaceMap.put(UUID.fromString(uuid), this.races.get(raceId));
        }
    }

    private void saveData() {
        plugin.getLogger().info("Сохранене " + playerRaceMap.size() + " записей");

        for (Map.Entry<UUID, Race> entry : playerRaceMap.entrySet()) {
            playerDataYaml.set(entry.getKey().toString(), entry.getValue().getId());
        }

        cfgManager.saveConfig(playerDataYaml);
    }

    public String getPlayerRaceId(Player player) {
        return getPlayerRace(player).getId();
    }

    public Race getPlayerRace(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerRaceMap.containsKey(uuid)) {
            playerRaceMap.put(uuid, this.races.get(getRandomRaceId()));
            saveData();
        }
        return playerRaceMap.get(player.getUniqueId());
    }

    public void setPlayerRace(Player player, String raceId) {
        if (!this.races.containsKey(raceId))
            throw new RuntimeException("Данная раса не существует! (" + raceId + ")");

        playerRaceMap.put(player.getUniqueId(), this.races.get(raceId));
        saveData();
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
