package dev.fdp.races;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RaceManager {
    private final File dataFile;
    private YamlConfiguration racesData;
    private final Map<String, String> nameToRaceMap= new HashMap<>();
    private final List<String> races = Arrays.asList("HUMAN", "ELF", "BEAR", "WOLF", "TIGER", "ORK");

    public RaceManager(File dataFolder) {
        this.dataFile = new File(dataFolder, "playerData.yml");
        loadData();
    }

    private void loadData() {
        if(!dataFile.exists()) {
            racesData = new YamlConfiguration();
        } else {
            racesData = YamlConfiguration.loadConfiguration(dataFile);
            for (String nicname : racesData.getKeys(false)) {
                nameToRaceMap.put(nicname.toLowerCase(), racesData.getString(nicname));
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
        int index = ThreadLocalRandom.current().nextInt(races.size());
        return races.get(index);
    }


    public List<String> getRaces() {
        return new ArrayList<>(races);
    }
}
