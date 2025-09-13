package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.RaceAttributeMapStorage;
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), task, 0, 20);
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
