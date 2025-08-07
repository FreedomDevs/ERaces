package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;

public class BowDamageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerBowDamage = new HashMap<>();
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
    public void onPlayerShoot(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof AbstractArrow arrow) {
            if (!bowArrows.contains(arrow.getUniqueId()))
                return;

            ProjectileSource shooter = arrow.getShooter();
            if (shooter instanceof Player player) {
                Double damage = playerBowDamage.get(player.getName());
                if (damage != null) {
                    double newDamage = event.getDamage() * damage;
                    event.setDamage(newDamage);
                }
            }

            bowArrows.remove(arrow.getUniqueId());
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageAxe = race.getWeaponProficiency().getBowDamageMultiplier();

        if (damageAxe == 1.0) {
            playerBowDamage.remove(player.getName());
        } else {
            playerBowDamage.put(player.getName(), damageAxe);
        }
    }

    @Override
    public void unload(Player player) {
        playerBowDamage.remove(player.getName());
    }
}
