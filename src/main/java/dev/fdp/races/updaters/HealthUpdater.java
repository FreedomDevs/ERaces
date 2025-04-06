package dev.fdp.races.updaters;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;

public class HealthUpdater implements Listener, IUpdater {
  @Override
  public void update(RaceManager raceManager, Player player) {
    String playerRace = raceManager.getPlayerRace(player.getName());

    Race race = raceManager.getRaces().get(playerRace);

    double maxHealth = race.getMaxHp();

    AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (attribute != null)
      attribute.setBaseValue(maxHealth);
  }
}
