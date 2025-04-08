package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeSpeedUpdator implements IUpdater, IUnloadable {
    private static Map<String, List<String>> biomeSpeed = new HashMap<>();

    @Override
    public void update(Race race, Player player) {
        String playerName = player.getName();

        if (race.getBiomeSpeed().size() == 0) {
            biomeSpeed.remove(playerName);
        } else {
            biomeSpeed.put(playerName, race.getBiomeSpeed());
        }
    }

    @Override
    public void unload(Player player) {
        biomeSpeed.remove(player.getName());
    }

    public void startTask(FDP_Races plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<String, List<String>> entry : biomeSpeed.entrySet()) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null || !player.isOnline()) continue;

                    List<String> biomes = entry.getValue();
                    Biome biome = player.getLocation().getWorld().getBiome(
                            player.getLocation().getBlockX(),
                            player.getLocation().getBlockY(),
                            player.getLocation().getBlockZ()
                    );

                    if (biomes.contains(biome.name())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 110, 1));
                    } else {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L);

    }
}
