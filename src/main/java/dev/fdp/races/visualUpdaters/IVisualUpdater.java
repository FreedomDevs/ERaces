package dev.fdp.races.visualUpdaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;

public interface IVisualUpdater {
    void updateVisuals(Player player, Race race);

    void unloadVisuals(Player player);
}
