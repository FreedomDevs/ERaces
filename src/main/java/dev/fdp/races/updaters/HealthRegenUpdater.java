package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class HealthRegenUpdater implements IUpdater, IUnloadable {
    private static final Map<String, Double> playerToRegen = new HashMap<>();
    private static Integer taskid = null;

    // TODO: зачем итератор?
    @Override
    public void update(Race race, Player player) {
        if (taskid == null) {
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), () -> {
                for (Iterator<Map.Entry<String, Double>> it = playerToRegen.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, Double> entry = it.next();
                    String playerName = entry.getKey();
                    double amount = entry.getValue();

                    Player player_arbuz = Bukkit.getPlayerExact(playerName);

                    if (player_arbuz != null && player_arbuz.isOnline() && !player_arbuz.isDead()) {
                        double newHealth = player_arbuz.getHealth() + amount;
                        player_arbuz
                                .setHealth(Math.min(newHealth, Objects.requireNonNull(player_arbuz.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));
                    } else {
                        // Удаляем мусор
                        it.remove();
                    }
                }

                if (playerToRegen.isEmpty()) {
                    Bukkit.getScheduler().cancelTask(taskid);
                    taskid = null;
                }
            }, 0, 20);
        }

        double regenPerSec = race.getRegenerationPerSec();
        if (regenPerSec == 0)
            playerToRegen.remove(player.getName());
        else
            playerToRegen.put(player.getName(), regenPerSec);
    }

    @Override
    public void unload(Player player) {
        playerToRegen.remove(player.getName());
    }
}
