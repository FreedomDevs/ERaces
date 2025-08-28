package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForbiddenFoodsUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, List<String>> forbiddenFoods = new HashMap<>();

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (forbiddenFoods.containsKey(event.getPlayer().getName())) {
            String item = event.getItem().getType().getKey().getKey();

            if (forbiddenFoods.get(event.getPlayer().getName()).contains(item)) {
                event.getPlayer()
                        .sendActionBar(
                                ChatUtil.format(FDP_Races.getMsgMng().getString("forbidden_foods"), Map.of()));

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

        if (race.getForbiddenFoods().isEmpty()) {
            forbiddenFoods.remove(playername);
        } else {
            forbiddenFoods.put(playername, race.getForbiddenFoods());
        }
    }

    @Override
    public void unload(Player player) {
        forbiddenFoods.remove(player.getName());
    }
}
