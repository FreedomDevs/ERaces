package dev.fdp.races.updaters;

import dev.fdp.races.Race;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BowSpeedUpdater implements Listener, IUpdater, IUnloadable {
  private static Map<String, Double> playerBowSpeedModifier = new HashMap<>();

  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    if (!(event.getEntity() instanceof Arrow))
      return;
    if (!(event.getEntity().getShooter() instanceof Player))
      return;

    Player player = (Player) event.getEntity().getShooter();
    String name = player.getName();

    if (!playerBowSpeedModifier.containsKey(name))
      return;

    double multiplier = playerBowSpeedModifier.get(name);
    Arrow arrow = (Arrow) event.getEntity();

    double originalSpeed = arrow.getVelocity().length();

    Vector direction = player.getLocation().getDirection().normalize();

    arrow.setVelocity(direction.multiply(originalSpeed * multiplier));
  }

  @Override
  public void update(Race race, Player player) {
    double bowSpeedModifier = race.getWeaponProficiency().getBowProjectileSpeedMultiplier();

    if (bowSpeedModifier == 0) {
      playerBowSpeedModifier.remove(player.getName());
    } else {
      playerBowSpeedModifier.put(player.getName(), bowSpeedModifier);
    }
  }

  @Override
  public void unload(Player player) {
    playerBowSpeedModifier.remove(player.getName());
  }
}
