package dev.elysium.eraces.updaters.base;

import dev.elysium.eraces.datatypes.Race;
import org.bukkit.entity.Player;

public interface IUpdater {
    void update(Race race, Player player);
}
