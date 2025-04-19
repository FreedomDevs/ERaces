package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class DamageWithWolfsNearUpdater implements Listener, IUpdater, IUnloadable {
    private static Map<String, Double> playerDamage = new HashMap<>();
    public static final double NEARBY_RADIUS = 8.5;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            String playerName = event.getDamager().getName();
            Player player = ((Player) event.getDamager());
            if (playerDamage.containsKey(playerName) && isWolfNearby(player, 8.5)) {
                double originalDamage = event.getDamage();
                double bonusDamage = playerDamage.get(playerName);
                double incrementDamage = originalDamage + bonusDamage;
                event.setDamage(incrementDamage);
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageHand = race.getWeaponProficiency().getDamageAdditionalWithWolfsNear();

        if (damageHand == 0) {
            playerDamage.remove(player.getName());
        } else {
            playerDamage.put(player.getName(), damageHand);
        }
    }

    private boolean isWolfNearby(Player player, double radius) {
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Wolf) {
                return true;
            }

            if (entity instanceof Player otherPlayer) {
                String race = FDP_Races.getInstance().raceManager.getPlayerRace(otherPlayer.getName());
                if (race.toLowerCase().contains("wolf")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void unload(Player player) {
        playerDamage.remove(player.getName());
    }
}
