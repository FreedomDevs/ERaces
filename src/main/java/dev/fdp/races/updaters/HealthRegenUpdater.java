package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.RaceAttributeMapStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class HealthRegenUpdater extends RaceAttributeMapStorage<Double> {
    {
        Runnable task = () -> {
            for (Map.Entry<UUID, Double> entry : map.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                Double amount = entry.getValue();
                if (player == null || !player.isOnline() || player.isDead()) continue;
                player.heal(amount);
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 20);
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getRegenerationPerSec();
    }

    @Override
    protected boolean putCheck(Double att) {
        return att != 0;
    }
}
