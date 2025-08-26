package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.MovementUtils;
import org.bukkit.entity.Player;

public class MovementSpeedUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        MovementUtils.setSpeedAttributeLevel(player, race.getMovementSpeedLevel());
    }
}
