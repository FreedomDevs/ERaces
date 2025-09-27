package dev.elysium.eraces.updaters;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AdditionalArmorUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        double additionalArmor = race.getAdditionalArmor();

        AttributeInstance attribute = player.getAttribute(Attribute.ARMOR);
        if (attribute != null)
            attribute.setBaseValue(additionalArmor);
    }
}
