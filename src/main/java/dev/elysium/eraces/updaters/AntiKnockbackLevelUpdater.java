package dev.elysium.eraces.updaters;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUpdater;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AntiKnockbackLevelUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        int antiKnockbackLevel = race.getAntiKnockbackLevel();

        AttributeInstance attribute = player.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (attribute != null)
            attribute.setBaseValue(antiKnockbackLevel);
    }
}
