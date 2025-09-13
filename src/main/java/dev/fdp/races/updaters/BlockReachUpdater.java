package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class BlockReachUpdater implements IUpdater {
    private static final Double BASE_HAND_DISTANCE = 4.5;

    @Override
    public void update(Race race, Player player) {
        double handDistanceBonus = race.getHandDistanceBonus();
        AttributeInstance attibute = player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE);
        if (attibute != null)
            attibute.setBaseValue(BASE_HAND_DISTANCE + handDistanceBonus);
    }
}
