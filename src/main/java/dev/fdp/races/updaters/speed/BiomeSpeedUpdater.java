package dev.fdp.races.updaters.speed;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.modifiers.ModifierAdapter;
import dev.fdp.races.modifiers.Modifiers;
import dev.fdp.races.updaters.base.RaceAttributeMapStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BiomeSpeedUpdater extends RaceAttributeMapStorage<List<String>> {
    private static final ModifierAdapter mod = Modifiers.SPEED.register();

    {
        Runnable task = () -> {
            for (Map.Entry<UUID, List<String>> entry : map.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player == null || !player.isOnline())
                    continue;

                String biome = player.getLocation().getWorld().getBiome(player.getLocation()).name();
                mod.set(player, entry.getValue().contains(biome) ? 2 : 0);
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 20);
    }


    @Override
    protected List<String> getAttribute(Race race) {
        return race.getBiomeSpeed();
    }

    @Override
    protected boolean putCheck(List<String> biomes) {
        return !biomes.isEmpty();
    }
}
