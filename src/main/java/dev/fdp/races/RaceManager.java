package dev.fdp.races;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

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

    public void loadData() {
        if (!dataFile.exists()) {
            racesData = new YamlConfiguration();
        } else {
            racesData = YamlConfiguration.loadConfiguration(dataFile);
            for (String nickname : racesData.getKeys(false)) {

                String racename = racesData.getString(nickname);

                if (!plugin.races.containsKey(racename)) {
                    plugin.getLogger().severe("ВНИМАНИЕ: РАССА: " + racename + " КУДАТ ПРОПАЛА (перегенерация...)");
                    racename = getRandomRace();
                }
                nameToRaceMap.put(nickname.toLowerCase(), racesData.getString(nickname));
            }
        }
    }

    private void saveData() {
        FDP_Races.getInstance().getLogger().info("Сохранене " + nameToRaceMap.size() + " записей");

        for (Map.Entry<String, String> entry : nameToRaceMap.entrySet()) {
            racesData.set(entry.getKey(), entry.getValue());
        }

        try {
            racesData.save(dataFile);
        } catch (IOException e) {
            FDP_Races.getInstance().getLogger().log(Level.SEVERE, "Failed to save data:", e);
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

    public void setPlayerRace(String nickname, String racename) {
        String loverNickname = nickname.toLowerCase();
        if (!plugin.races.containsKey(racename))
            throw new RuntimeException("Данная расса не существует! (" + racename + ")");

        nameToRaceMap.put(loverNickname, racename);
        saveData();
    }

    public String getRandomRace() {
        List<String> raceIds = new ArrayList<>(plugin.races.keySet());
        return raceIds.get(ThreadLocalRandom.current().nextInt(raceIds.size()));
    }

    public Map<String, Race> getRaces() {
        return plugin.races;
    }

    public Map<String, String> getNameToRaceMap() {
        return nameToRaceMap;
    }
}
