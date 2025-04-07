package dev.fdp.races.updaters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import dev.fdp.races.utils.ChatUtil;

public class ForbiddenFoodsUpdater implements Listener, IUpdater {
  private static Map<String, List<String>> forbiddenFoods = new HashMap<>();

  @EventHandler
  public void onPlayerEat(PlayerItemConsumeEvent event) {
    if (forbiddenFoods.containsKey(event.getPlayer().getName())) {
      String item = event.getItem().getType().getKey().getKey();
      Bukkit.getLogger().info(item);

      if (forbiddenFoods.get(event.getPlayer().getName()).contains(item)) {
        event.getPlayer()
            .sendActionBar(
                ChatUtil.format(FDP_Races.getInstance().messageManager.getString("forbidden_foods"), Map.of()));

        event.getPlayer().playSound(
            event.getPlayer().getLocation(),
            Sound.ENTITY_VILLAGER_NO,
            1.0f,
            1.0f);

        event.getPlayer().spawnParticle(Particle.ANGRY_VILLAGER, event.getPlayer().getLocation(), 100, 0.5, 0.7, 0.5,
            0.4);

        event.setCancelled(true);
      }
    }
  }

  @Override
  public void update(Race race, Player player) {
    String playername = player.getName();

    if (race.getForbiddenFoods().size() == 0) {
      forbiddenFoods.remove(playername);
    } else {
      forbiddenFoods.put(playername, race.getForbiddenFoods());
    }
  }
}
