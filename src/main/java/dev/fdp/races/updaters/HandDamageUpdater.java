package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class HandDamageUpdater implements Listener, IUpdater {
    private static Map<String, Double> playerHandDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        String playerName = player.getName();
        if (playerHandDamage.containsKey(playerName)) {
            double originalDamage = event.getDamage();
            double bonusDamage =  playerHandDamage.get(playerName);
            double incrementDamage = originalDamage + bonusDamage;
            event.setDamage(incrementDamage);
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageHand = race.getWeaponProficiency().getHandDamageAdditional();

        if (damageHand == 0) {
            playerHandDamage.remove(player.getName());
        } else {
            playerHandDamage.put(player.getName(), damageHand);
        }
    }
}
