package dev.fdp.races.updaters;

import dev.fdp.races.Race;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class MaceDamageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerMaceDamage = new HashMap<>();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerMaceDamage.containsKey(playerName)) {
                if (player.getInventory().getItemInMainHand().getType() == Material.MACE) {
                    double damage = event.getDamage();
                    double newDamage = damage * playerMaceDamage.get(playerName);
                    event.setDamage(newDamage);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageMace = race.getWeaponProficiency().getMaceDamageMultiplier();

        if (damageMace == 1.0) {
            playerMaceDamage.remove(player.getName());
        } else {
            playerMaceDamage.put(player.getName(), damageMace);
        }
    }

    @Override
    public void unload(Player player) {
        playerMaceDamage.remove(player.getName());
    }
}
