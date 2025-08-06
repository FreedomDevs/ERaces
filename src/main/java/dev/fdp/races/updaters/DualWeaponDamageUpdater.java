package dev.fdp.races.updaters;

import dev.fdp.races.Race;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class DualWeaponDamageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerDualDamage = new HashMap<>();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerDualDamage.containsKey(playerName)) {
                if (player.getInventory().getItemInMainHand().getType() == player.getInventory().getItemInOffHand()
                        .getType()) {
                    double damage = event.getDamage();
                    double newDamage = damage + playerDualDamage.get(playerName);
                    event.setDamage(newDamage);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageDual = race.getWeaponProficiency().getDualWeaponDamageAdditional();

        if (damageDual == 1.0) {
            playerDualDamage.remove(player.getName());
        } else {
            playerDualDamage.put(player.getName(), damageDual);
        }
    }

    @Override
    public void unload(Player player) {
        playerDualDamage.remove(player.getName());
    }
}
