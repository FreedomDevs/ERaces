package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AdditionalArmorUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        double additionalArmor = race.getAdditionalArmor();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ARMOR);
        if (attibute != null)
            attibute.setBaseValue(additionalArmor);
    }
}
