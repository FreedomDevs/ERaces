package dev.fdp.races.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;

public class HeathUpdater implements Listener {

  private final RaceManager raceManager;

  public HeathUpdater(RaceManager raceManager) {
    this.raceManager = raceManager;
  }

  public void updateHealth(Player player) {
    String playerRace = raceManager.getPlayerRace(player.getName());

    Race race = raceManager.getRaces().get(playerRace);

    double maxHealth = race.getMaxHp();

    AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (attribute != null)
      attribute.setBaseValue(maxHealth);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    updateHealth(event.getPlayer());
  }

}
