package dev.elysium.eraces.visualUpdaters;

import dev.elysium.eraces.datatypes.Race;
import org.bukkit.entity.Player;

public interface IVisualUpdater {
    void updateVisuals(Race race, Player player);

    void unloadVisuals(Player player);
}
