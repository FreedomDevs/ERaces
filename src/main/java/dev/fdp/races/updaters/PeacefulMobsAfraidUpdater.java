package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PeacefulMobsAfraidUpdater implements IUpdater, IUnloadable {
    private static final Set<Player> peacefulMobsAfraid = new HashSet<>();
    private static Integer taskid = null;

    @Override
    public void update(Race race, Player player) {
        if (taskid == null)
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 20);

        if (race.isPeacefulMobsAfraid()) peacefulMobsAfraid.add(player);
        else peacefulMobsAfraid.remove(player);
    }

    @Override
    public void unload(Player player) {
        peacefulMobsAfraid.remove(player);
    }

    Runnable task = () -> {
        for (Player player : Bukkit.getOnlinePlayers())
            if (peacefulMobsAfraid.contains(player)) peacefulMobsAfraidFromPlayer(player);
    };

    private void peacefulMobsAfraidFromPlayer(Player player) {
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (isPeacefulMob(entity)) {
                // TODO: reasonable panic mode
                LivingEntity mob = (LivingEntity) entity;

                Vector direction = mob.getLocation().toVector().subtract(player.getLocation().toVector());

                direction.normalize().multiply(0.5);
                mob.setVelocity(direction);

                try {
                    mob.setAI(true);
                } catch (NoSuchMethodError ignored) {
                }
            }
        }
    }

    private boolean isPeacefulMob(Entity entity) {
        return (entity instanceof Animals && !(entity instanceof Enemy));
    }
}