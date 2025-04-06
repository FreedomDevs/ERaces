package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class AdditionalArmorUpdater implements Listener, IUpdater {
    @Override
    public void update(Race race, Player player) {
        double additionalArmor = race.getAdditionalArmor();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ARMOR);
        if (attibute != null) {
            attibute.setBaseValue(additionalArmor);
        }
    }
}
