package dev.fdp.races.visualUpdaters;

import dev.fdp.races.Race;
import dev.fdp.races.FDP_Races;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EarsUpdater implements IVisualUpdater {

    private final Map<UUID, BukkitRunnable> tasks = new HashMap<>();

    @Override
    public void updateVisuals(Player player, Race race) {
        if (race.getVisuals().contains("ears")) {
            if (tasks.containsKey(player.getUniqueId())) return;

            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        tasks.remove(player.getUniqueId());
                        return;
                    }

                    Location loc = player.getEyeLocation();

                    Location leftEar = loc.clone().add(-0.3, 0.3, 0);
                    Location rightEar = loc.clone().add(0.3, 0.3, 0);

                    DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 200, 0), 1.2f);

                    player.spawnParticle(Particle.DUST, leftEar, 5, 0.1, 0.1, 0.1, 0, dustOptions);
                    player.spawnParticle(Particle.DUST, rightEar, 5, 0.1, 0.1, 0.1, 0, dustOptions);
                }
            };

            task.runTaskTimer(FDP_Races.getInstance(), 0L, 10L);
            tasks.put(player.getUniqueId(), task);
        } else {
            unloadVisuals(player);
        }
    }

    @Override
    public void unloadVisuals(Player player) {
        BukkitRunnable task = tasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }
}
