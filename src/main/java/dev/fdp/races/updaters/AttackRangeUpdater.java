package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class AttackRangeUpdater implements Listener, IUpdater {
    @Override
    public void update(RaceManager raceManager, Player player) {
        String playerRace = raceManager.getPlayerRace(player.getName());
        Race race = raceManager.getRaces().get(playerRace);
        int attackRange = race.getHandDistanceBonus();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE); // Код немного не работает
        if (attibute != null) {
            double baseAttribute = attibute.getBaseValue();
            attibute.setBaseValue(baseAttribute + attackRange);
        }
    }
}
