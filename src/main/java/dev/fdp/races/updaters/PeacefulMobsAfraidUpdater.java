package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class PeacefulMobsAfraidUpdater implements IUpdater, IUnloadable {
    private static Set<Player> peacefulMobsAfraid = new HashSet<>();
    private static Integer taskid = null;

    @Override
    public void update(Race race, Player player) {
        if (taskid == null)
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 20);

        if (race.isPeacefulMobsAfraid())
            peacefulMobsAfraid.add(player);
        else
            peacefulMobsAfraid.remove(player);
    }

    @Override
    public void unload(Player player) {
        peacefulMobsAfraid.remove(player);
    }

    Runnable task = () -> {
        for (Player player : Bukkit.getOnlinePlayers())
            if (peacefulMobsAfraid.contains(player))
                peacefulMobsAfraidFromPlayer(player);
    };

    private void peacefulMobsAfraidFromPlayer(Player player) {
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity && isPeacefulMob(entity.getType())) {
                LivingEntity mob = (LivingEntity) entity;

                Vector direction = mob.getLocation().toVector()
                        .subtract(player.getLocation().toVector());

                direction.normalize().multiply(0.5);
                mob.setVelocity(direction);

                try {
                    mob.setAI(true);
                } catch (NoSuchMethodError e) {
                }
            }
        }
    }

    private boolean isPeacefulMob(EntityType type) {
        return type == EntityType.COW ||
                type == EntityType.PIG ||
                type == EntityType.SHEEP ||
                type == EntityType.CHICKEN ||
                type == EntityType.RABBIT ||
                type == EntityType.LLAMA ||
                type == EntityType.FOX ||
                type == EntityType.PANDA ||
                type == EntityType.TURTLE ||
                type == EntityType.OCELOT ||
                type == EntityType.WOLF ||
                type == EntityType.CAT;
    }
}
