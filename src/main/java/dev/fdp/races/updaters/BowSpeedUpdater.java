package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class BowSpeedUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerBowSpeedModifier = new HashMap<>();
    private static final Set<UUID> bowArrows = new HashSet<>();

    @EventHandler
    public void onPlayerBowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getBow() != null) {
            if (event.getBow().getType() == Material.BOW && event.getProjectile() instanceof AbstractArrow) {
                bowArrows.add(event.getProjectile().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.getShooter() instanceof Player player) {
                if (bowArrows.contains(arrow.getUniqueId())) {
                    bowArrows.remove(arrow.getUniqueId());

                    Double multiplier = playerBowSpeedModifier.get(player.getName());

                    if (multiplier != null && multiplier != 0.0) {
                        Vector direction = arrow.getVelocity();
                        arrow.setVelocity(direction.multiply(multiplier));
                    }
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double bowSpeedModifier = race.getWeaponProficiency().getBowProjectileSpeedMultiplier();

        if (bowSpeedModifier == 0) {
            playerBowSpeedModifier.remove(player.getName());
        } else {
            playerBowSpeedModifier.put(player.getName(), bowSpeedModifier);
        }
    }

    @Override
    public void unload(Player player) {
        playerBowSpeedModifier.remove(player.getName());
    }
}
