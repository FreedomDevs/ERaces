package dev.elysium.eraces;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.visualUpdaters.EarsUpdater;
import dev.elysium.eraces.visualUpdaters.IVisualUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class VisualsManager {
    private static final List<IVisualUpdater> visualUpdaters = List.of(
            new EarsUpdater()
    );

    public static void updateVisualsForPlayer(Player player) {
        Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);

        for (IVisualUpdater updater : visualUpdaters)
            updater.updateVisuals(race, player);
    }

    public static void reloadVisualsForAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers())
            updateVisualsForPlayer(player);
    }

    public static void unloadVisualsForPlayer(Player player) {
        for (IVisualUpdater updater : visualUpdaters)
            updater.unloadVisuals(player);
    }
}