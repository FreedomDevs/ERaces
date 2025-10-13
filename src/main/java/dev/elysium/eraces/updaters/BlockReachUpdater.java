package dev.elysium.eraces.updaters;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class BlockReachUpdater implements IUpdater {
    private static final Double BASE_HAND_DISTANCE = 4.5;

    @Override
    public void update(Race race, Player player) {
        double handDistanceBonus = race.getHandDistanceBonus();
        AttributeInstance attribute = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        if (attribute != null)
            attribute.setBaseValue(BASE_HAND_DISTANCE + handDistanceBonus);
    }
}
