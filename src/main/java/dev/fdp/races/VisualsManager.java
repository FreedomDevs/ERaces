package dev.fdp.races;

import dev.fdp.races.visualUpdaters.EarsUpdater;
import dev.fdp.races.visualUpdaters.IVisualUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class VisualsManager {
    private static final List<IVisualUpdater> visualUpdaters = List.of(
            new EarsUpdater()
    );

    public static void updateVisualsForPlayer(Player player) {
        RaceManager raceManager = FDP_Races.getInstance().raceManager;
        Race race = raceManager.getRaceForPlayer(player);

        for (IVisualUpdater updater : visualUpdaters) {
            updater.updateVisuals(player, race);
        }
    }

    public static void reloadVisualsForAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateVisualsForPlayer(player);
        }
    }

    public static void unloadVisualsForPlayer(Player player) {
        for (IVisualUpdater updater : visualUpdaters) {
            updater.unloadVisuals(player);
        }
    }
}