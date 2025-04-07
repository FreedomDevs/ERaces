package dev.fdp.races.updaters;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import dev.fdp.races.Race;

public class HealthUpdater implements IUpdater {
  @Override
  public void update(Race race, Player player) {
    double maxHealth = race.getMaxHp();

    AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (attribute != null)
      attribute.setBaseValue(maxHealth);
  }
}
