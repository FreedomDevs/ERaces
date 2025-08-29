package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class HealthUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        double maxHealth = race.getMaxHp();

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null)
            attribute.setBaseValue(maxHealth);
    }
}
