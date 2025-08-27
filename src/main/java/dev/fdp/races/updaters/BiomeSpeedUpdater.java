package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.modifiers.ModifierAdapter;
import dev.fdp.races.modifiers.Modifiers;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeSpeedUpdater implements IUpdater, IUnloadable {
    private static final Map<String, List<String>> biomeSpeed = new HashMap<>();
    private static Integer taskid = null;
    final ModifierAdapter mod = Modifiers.SPEED.register();


    @Override
    public void update(Race race, Player player) {
        String playerName = player.getName();

        if (taskid == null)
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 180);

        if (race.getBiomeSpeed().isEmpty())
            biomeSpeed.remove(playerName);
        else
            biomeSpeed.put(playerName, race.getBiomeSpeed());
    }

    @Override
    public void unload(Player player) {
        biomeSpeed.remove(player.getName());
    }

    private final Runnable task = () -> {
        for (Map.Entry<String, List<String>> entry : biomeSpeed.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null || !player.isOnline())
                continue;

            List<String> biomes = entry.getValue();
            Biome biome = player.getLocation().getWorld().getBiome(player.getLocation());

            mod.set(player, biomes.contains(biome.name()) ? 2 : 0);
        }
    };
}
