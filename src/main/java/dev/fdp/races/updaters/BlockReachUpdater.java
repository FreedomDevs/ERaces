package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BlockReachUpdater implements IUpdater, IUnloadable, Listener {
    private static final Double defaultHandDistance = 4.5;

    @Override
    public void update(Race race, Player player) {
        double handDistanceBonus = race.getHandDistanceBonus();
        player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).setBaseValue(defaultHandDistance + handDistanceBonus);
    }

    @Override
    public void unload(Player player) {
        player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).setBaseValue(defaultHandDistance);
    }
}
