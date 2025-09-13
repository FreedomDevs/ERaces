package dev.elysium.eraces.updaters.speed;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.modifiers.ModifierAdapter;
import dev.elysium.eraces.modifiers.Modifiers;
import dev.elysium.eraces.updaters.base.RaceAttributeMapStorage;
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), task, 0, 20);
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
