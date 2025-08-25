package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.ItemChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HandDamageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Map<String, Double> playerHandDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerHandDamage.containsKey(playerName)) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (!ItemChecker.isTool(item.getType())) {
                    double originalDamage = event.getDamage();
                    double bonusDamage = playerHandDamage.get(playerName);
                    double incrementDamage = originalDamage + bonusDamage;
                    event.setDamage(incrementDamage);
                }
            }
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

    @Override
    public void unload(Player player) {
        playerHandDamage.remove(player.getName());
    }
}
