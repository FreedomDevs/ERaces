package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AntiKnockbackLevelUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        int antiKnockbackLevel = race.getAntiKnockbackLevel();

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (attribute != null)
            attribute.setBaseValue(antiKnockbackLevel);
    }
}
