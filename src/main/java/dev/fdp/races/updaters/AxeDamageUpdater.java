package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class AxeDamageUpdater implements Listener,IUpdater, IUnloadable {
    private static final Map<String, Double> playerAxeDamage = new HashMap<>();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerAxeDamage.containsKey(playerName)) {
                if (player.getInventory().getItemInMainHand().getType().toString().endsWith("_AXE")) {
                    double damage = event.getDamage();
                    double newDamage = damage * playerAxeDamage.get(playerName);
                    event.setDamage(newDamage);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageAxe = race.getWeaponProficiency().getAxeDamageMultiplier();

        if (damageAxe == 1.0) {
            playerAxeDamage.remove(player.getName());
        } else {
            playerAxeDamage.put(player.getName(), damageAxe);
        }
    }

    @Override
    public void unload(Player player) {
        playerAxeDamage.remove(player.getName());
    }
}
