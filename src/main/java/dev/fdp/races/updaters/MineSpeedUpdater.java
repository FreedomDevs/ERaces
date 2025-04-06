package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MineSpeedUpdater implements Listener, IUpdater {
    @Override
    public void update(Race race, Player player) {
        double mineSpeed = race.getMineSpeed();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED); // Не рабтает тоже
        if (attibute != null) {
            double baseAttibute = attibute.getBaseValue();
            attibute.setBaseValue(baseAttibute * mineSpeed);
        }
    }
}
