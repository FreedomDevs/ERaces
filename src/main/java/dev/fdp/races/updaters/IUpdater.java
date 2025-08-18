package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;

public interface IUpdater {
    void update(Race race, Player player);
}
