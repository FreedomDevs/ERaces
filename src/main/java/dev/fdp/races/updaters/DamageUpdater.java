package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class DamageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            String playerName = event.getDamager().getName();
            if (playerDamage.containsKey(playerName)) {
                double originalDamage = event.getDamage();
                double bonusDamage = playerDamage.get(playerName);
                double incrementDamage = originalDamage + bonusDamage;
                event.setDamage(incrementDamage);
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        double damageHand = race.getWeaponProficiency().getDamageAdditional();

        if (damageHand == 0) {
            playerDamage.remove(player.getName());
        } else {
            playerDamage.put(player.getName(), damageHand);
        }
    }

    @Override
    public void unload(Player player) {
        playerDamage.remove(player.getName());
    }
}
