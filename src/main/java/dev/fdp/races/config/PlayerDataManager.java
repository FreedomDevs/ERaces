package dev.fdp.races.config;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDataManager {
    private static final String FILE_NAME = "playerData.yml";
    private final YamlManager cfgManager;
    private YamlConfiguration racesData;
    private final Map<String, String> nameToRaceMap = new HashMap<>();
    private final FDP_Races plugin;


    public PlayerDataManager(FDP_Races plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(plugin, FILE_NAME, false);
        loadData();
    }

    public void loadData() {
        racesData = cfgManager.getConfig();
        for (String nickname : racesData.getKeys(false)) {
            String racename = racesData.getString(nickname);

            if (!plugin.getRacesConfigManager().getRaces().containsKey(racename)) {
                plugin.getLogger().severe("ВНИМАНИЕ: РАСА: " + racename + " КУДАТ ПРОПАЛА (перегенерация...)");
                racename = getRandomRace();
            }
            nameToRaceMap.put(nickname, racename);
        }
    }

    private void saveData() {
        FDP_Races.getInstance().getLogger().info("Сохранене " + nameToRaceMap.size() + " записей");

        for (Map.Entry<String, String> entry : nameToRaceMap.entrySet()) {
            racesData.set(entry.getKey(), entry.getValue());
        }

        cfgManager.saveConfig(racesData);
    }

    public String getPlayerRaceId(String nickname) {
        if (!nameToRaceMap.containsKey(nickname)) {
            nameToRaceMap.put(nickname, getRandomRace());
            saveData();
        }
        return nameToRaceMap.get(nickname);
    }

    public Race getPlayerRace(String nickname) {
        return plugin.getRacesConfigManager().getRaces().get(getPlayerRaceId(nickname));
    }

    public void setPlayerRace(String nickname, String raceId) {
        if (!plugin.getRacesConfigManager().getRaces().containsKey(raceId))
            throw new RuntimeException("Данная раса не существует! (" + raceId + ")");

        nameToRaceMap.put(nickname, raceId);
        saveData();
    }

    public String getRandomRace() {
        List<String> raceIds = plugin.getRacesConfigManager().getRaces().entrySet().stream()
                .filter(entry -> !entry.getValue().isExcludeFromRandom())
                .map(Map.Entry::getKey)
                .toList();

        if (raceIds.isEmpty())
            throw new NullPointerException("Нету рас для рандомной выдачи");

        return raceIds.get(ThreadLocalRandom.current().nextInt(raceIds.size()));
    }
}
