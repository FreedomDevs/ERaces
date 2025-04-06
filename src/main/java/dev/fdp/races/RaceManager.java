package dev.fdp.races;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RaceManager {
    private final File dataFile;
    private YamlConfiguration racesData;
    private final Map<String, String> nameToRaceMap = new HashMap<>();
    private final FDP_Races plugin;

    public RaceManager(File dataFolder, FDP_Races plugin) {
        this.dataFile = new File(dataFolder, "playerData.yml");
        this.plugin = plugin;
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            racesData = new YamlConfiguration();
        } else {
            racesData = YamlConfiguration.loadConfiguration(dataFile);
            for (String nickname : racesData.getKeys(false)) {

                String racename = racesData.getString(nickname);

                if (!plugin.races.containsKey(racename)) {
                    plugin.getLogger().severe("ВНИМАНИЕ: РАССА: " + racename + " КУДАТ ПРОПАЛА (перегенерация...)");
                    racename = getPlayerRace(nickname);
                }
                nameToRaceMap.put(nickname.toLowerCase(), racesData.getString(nickname));
            }
        }
    }

    private void saveData() {
        for (Map.Entry<String, String> entry : nameToRaceMap.entrySet()) {
            racesData.set(entry.getKey(), entry.getValue());
        }

        try {
            racesData.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerRace(String nickname) {
        String loverNickname = nickname.toLowerCase();
        if (!nameToRaceMap.containsKey(loverNickname)) {
            String randomRace = getRandomRace();
            nameToRaceMap.put(loverNickname, randomRace);
            saveData();
            return randomRace;
        }
        return nameToRaceMap.get(loverNickname);
    }

    private String getRandomRace() {
        List<String> raceIds = new ArrayList<>(plugin.races.keySet());
        return raceIds.get(ThreadLocalRandom.current().nextInt(raceIds.size()));
    }

    public Map<String, Race> getRaces() {
        return plugin.races;
    }
}
