package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Map;

public class BowDamageUpdater implements Listener, IUpdater, IUnloadable {
    private static Map<String, Double> playerBowDamage = new HashMap<>();

    @EventHandler
    public void onPlayerShoot(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) event.getDamager();
            ProjectileSource shooter = arrow.getShooter();
            if (shooter instanceof Player) {
                Player player = (Player) shooter;
                if (playerBowDamage.containsKey(player.getName())) {
                    double damage = event.getDamage();
                    double newDamage = damage * playerBowDamage.get(player.getName());
                    event.setDamage(newDamage);
                }
            }
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
