package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class SwordDamageUpdater implements Listener, IUpdater,IUnloadable {
    private static Map<String, Double> playerSwordDamage = new HashMap<>();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player) {
            String playerName = event.getDamager().getName();
            Player player = (Player) event.getDamager();
            if (playerSwordDamage.containsKey(playerName)) {
                if (player.getInventory().getItemInMainHand().getType().toString().endsWith("_SWORD")) {
                    double damage = event.getDamage();
                    double newDamage = damage * playerSwordDamage.get(playerName);
                    event.setDamage(newDamage);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageSword = race.getWeaponProficiency().getSwordDamageMultiplier();

        if (damageSword == 1.0) {
            playerSwordDamage.remove(player.getName());
        } else {
            playerSwordDamage.put(player.getName(), damageSword);
        }
    }

    @Override
    public void unload(Player player) {
        playerSwordDamage.remove(player.getName());
    }
}
