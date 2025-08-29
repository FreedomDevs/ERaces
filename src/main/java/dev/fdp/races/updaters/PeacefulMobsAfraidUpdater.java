package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.IUnloadable;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;

import java.util.HashSet;
import java.util.Set;

public class PeacefulMobsAfraidUpdater implements IUpdater, IUnloadable {
    private static final Set<Player> peacefulMobsAfraid = new HashSet<>();
    private static Integer taskid = null;

    @Override
    public void update(Race race, Player player) {
        if (taskid == null)
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(FDP_Races.getInstance(), task, 0, 45);

        if (race.isPeacefulMobsAfraid()) peacefulMobsAfraid.add(player);
        else peacefulMobsAfraid.remove(player);
    }

    @Override
    public void unload(Player player) {
        peacefulMobsAfraid.remove(player);
    }

    private final Runnable task = () -> {
        for (Player player : peacefulMobsAfraid)
            if (player.isOnline())
                peacefulMobsAfraidFromPlayer(player);
    };

    private void peacefulMobsAfraidFromPlayer(Player player) {
        Race race = FDP_Races.getPlayerMng().getPlayerRace(player);
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (isPeacefulMob(entity, race))
                ((Damageable) entity).damage(0, DamageSource.builder(DamageType.PLAYER_ATTACK).build());
        }
    }

    private boolean isPeacefulMob(Entity entity, Race race) {
        return (entity instanceof Animals && !(entity instanceof Enemy))
                && !race.getAfraidMobsExceptions().contains(entity.getType().toString());
    }
}